package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

/**
 * This class is a container for Faq database
 * @author Group 16 
 */
@Slf4j
public class FaqDatabase extends SQLDatabaseEngine {

	/**
	 * Replys user with a image
	 * 
	 * @param answer		a parameter which indactes the need of replying a image
	 * @return 			url of image
	 */
	public String replyImage(String answer) {
		// String pattern = "(\\d+)[.](.*[?])[\\n][>](.*)[\\n]";
		String pattern = "see the picture (.*)[)]";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(answer);
		if (m.find()) {
			return m.group(1);
		} else
			return null;
	}


	/**
	 * Loads question from database
	 * 
	 * @return		faq question from databse
	 * @see 			faqEntry
	 */
	public List<faqEntry> loadQuestion() throws Exception{
		//return loadQuestionStatic();
		return loadQuestionSQL();

	}

	/**
	 * Loads question from database
	 * 
	 * @return		faq question from databse
	 * @see 			faqEntry
	 * @throws		Exception when databse is not accessed successfully
	 */
	public List<faqEntry> loadQuestionSQL() throws Exception{
		try {
			Connection connection = super.getConnection();
			List<faqEntry> listOfEntry = new ArrayList<faqEntry>();
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT line_faq.id, line_faq.question, line_faq.answer, line_faq.hit, line_keyword.keyword_text "
							+ "FROM line_faq, line_keyword, line_faq_keyword WHERE line_faq.id=line_faq_keyword.faq_id AND "
							+ "line_keyword.id=line_faq_keyword.keyword_id;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				faqEntry entry = new faqEntry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(5),
						rs.getInt(4));
				listOfEntry.add(entry);
				// PreparedStatement stmt2 = connection.prepareStatement("UPDATE keywords SET
				// hit = ? WHERE question = ?;");
			}
			rs.close();
			stmt.close();
			connection.close();

			log.info("faq database load successfully");
			return listOfEntry;
		} catch (Exception e) {
			log.info("Exception while reading database: {}", e.toString());
			return null;
		}

		// if (listOfEntry != null && !listOfEntry.isEmpty())
		// log.info("faq database probably empty");
		// throw new Exception("EMPTY DATABASE");
	}

	/**
	 * Updates hit number given the question id
	 * 
	 * @param qid	question id
	 * @param hit	the hit number to be updated to
	 * @return		indicates whether updateHit function has been successfully called
	 * @throws		Exception when databse is not accessed successfully
	 */
	public boolean updateHit(int qid, int hit) throws Exception {
		try {
			Connection connection = super.getConnection();
			int result = 0;
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_faq SET hit = ? WHERE id = ?;");
			stmt2.setInt(1, hit);
			stmt2.setInt(2, qid);
			result = stmt2.executeUpdate();
			stmt2.close();
			connection.close();

			log.info("update hit successfully");
			return true;
		} catch (Exception e) {
			log.info("Exception while reading database: {}", e.toString());
			return false;
		}
	}

	/**
	 * Searchs answer from FAQ list
	 * 
	 * @param text		the question which user input
	 * @param userID		the id of the user
	 * @return			the answer of the question which user asks
	 * @see				faqEntry
	 * @throws			Exception when there is no answer for the given question
	 */

	public String search(String text, String userID) throws Exception {

		String result = null;

		List<faqEntry> listOfEntry = loadQuestion();
		int qid = -1;
		int hit = 0;
		// using wagnerFischer Algorithm to select question within 10 unit distance
//		if (!listOfEntry.isEmpty()) {
			int dist;
			int minDistance = 1000000;
			for (faqEntry entry : listOfEntry) {
				dist = new WagnerFischer(entry.Question, text).getDistance();
				if (dist <= 2 && dist < minDistance) {
					minDistance = dist;
					result = entry.Answer;
					qid = entry.questionID;
					hit = entry.hit;
				}
			}
			if (result == null) {
				for (faqEntry entry : listOfEntry) {
					if (text.toLowerCase().contains(entry.Keyword.toLowerCase())) {
						dist = new WagnerFischer(entry.Question, text).getDistance();
						if (dist < minDistance) {
							minDistance = dist;
							if (result == null) {
								result = entry.Answer + "\n\n";
							} else {
								result += entry.Answer + "\n\n";
							}
							qid = entry.questionID;
							hit = entry.hit;
//							if (qid != -1) {
								updateHit(qid, hit + 1);
//							}
						}
					}
				}
			}

			// dynamic question
			if (result.toLowerCase().contains("return")) {

				if (result.toLowerCase().contains("top 5 tours")) {
					result = dynamic_top_five_tours(userID);
				} else if (result.toLowerCase().contains("more tours")) {
					result = dynamic_more_tours(userID);
				} else if (result.toLowerCase().contains("hot spring")) {
					result = dynamic_hot_spring();
				} else if (result.toLowerCase().contains("mountain")) {
					result = dynamic_mountain();
				}
			}
//		}

		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}

	
	/**
	 * Returns result of the top five tours
	 * 
	 * @param userID		the id of the user
	 * @return			result of the top five tours without including the tours which the user has visited
	 * @throws			Exception when database is not accessed successfully
	 */
	
	public String dynamic_top_five_tours(String userID) {
		try {
			String result = "";
			Connection connection = super.getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT line_tour.id, line_tour.name, line_tour.description, SUM (line_booking.adult_num+line_booking.child_num+line_booking.toddler_num) "
							+ "FROM line_booking, line_touroffering, line_tour "
							+ "WHERE line_booking.\"tourOffering_id\"=line_touroffering.id AND line_touroffering.tour_id=line_tour.id "
							+ "AND line_booking.state > 0 " + "AND line_tour.id NOT IN "
							+ "(SELECT line_tour.id FROM line_booking, line_touroffering, line_tour "
							+ "WHERE line_booking.user_id = ? AND line_booking.state = 2 AND "
							+ "line_booking.\"tourOffering_id\"=line_touroffering.id AND line_touroffering.tour_id=line_tour.id) "
							+ "GROUP BY line_tour.id, line_tour.name, line_tour.description "
							+ "ORDER BY SUM (line_booking.adult_num+line_booking.child_num+line_booking.toddler_num) DESC;");
			stmt.setString(1, userID);
			ResultSet rs = stmt.executeQuery();
			int i = 0;
			while (rs.next() && i < 5) {
				result += (rs.getString(1) + " " + rs.getString(2) + "\n" + rs.getString(3) + "\n"
						+ "Number of people who have visited: " + rs.getInt(4) + "\n\n");
				i++;
			}
			if (result.equals("")) {
				result = "You have been to all tours we provided. Thanks for your interest and support!\n";
			} else {
				String header = "The following are hot tours we recommend based on your booking history.\n\n";
				result = header + result;
			}
			rs.close();
			stmt.close();
			connection.close();
			return result;
		} catch (Exception e) {
			log.info("Exception while reading database: {}", e.toString());
			return null;
		}
	}

	
	/**
	 * Returns more tours after user has asked about hot tours
	 * 
	 * @param userID		the id of the user
	 * @return			more tours which are popular without including the tours which the user has visited
	 * @throws			Exception when database is not accessed successfully
	 */
	
	public String dynamic_more_tours(String userID) {
		try {
			String result = "";
			Connection connection = super.getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT line_tour.id, line_tour.name, line_tour.description, SUM (line_booking.adult_num+line_booking.child_num+line_booking.toddler_num) "
							+ "FROM line_booking, line_touroffering, line_tour "
							+ "WHERE line_booking.\"tourOffering_id\"=line_touroffering.id AND line_touroffering.tour_id=line_tour.id "
							+ "AND line_booking.state > 0 " + "AND line_tour.id NOT IN "
							+ "(SELECT line_tour.id FROM line_booking, line_touroffering, line_tour "
							+ "WHERE line_booking.user_id = ? AND line_booking.state = 2 AND "
							+ "line_booking.\"tourOffering_id\"=line_touroffering.id AND line_touroffering.tour_id=line_tour.id) "
							+ "GROUP BY line_tour.id, line_tour.name, line_tour.description "
							+ "ORDER BY SUM (line_booking.adult_num+line_booking.child_num+line_booking.toddler_num) DESC;");
			stmt.setString(1, userID);
			ResultSet rs = stmt.executeQuery();
			ResultSet temp = rs;
			int i = 0;
			while (i < 5 && rs.next()) {
				i++;
			}
			while (rs.next()) {
				result += (rs.getString(1) + " " + rs.getString(2) + "\n" + rs.getString(3) + "\n"
						+ "Number of people who have visited: " + rs.getInt(4) + "\n\n");
			}
			if (result.equals("")) {
				result = "No more recommendations! Thanks for your interest and support!\n";
			} else {
				String header = "The following are more hot tours we recommend based on your booking history.\n\n";
				result = header + result;
			}
			rs.close();
			stmt.close();
			connection.close();
			return result;
		} catch (Exception e) {
			log.info("Exception while reading database: {}", e.toString());
			return null;
		}
	}

	
	/**
	 * Returns tours which provide hot spring service
	 * 
	 * @return			tours which provide hot spring service
	 * @throws			Exception when database is not accessed successfully
	 */
	
	public String dynamic_hot_spring() {
		try {
			String result = "";
			Connection connection = super.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT line_tour.id, line_tour.name, line_tour.description " + "FROM line_tour "
							+ "WHERE line_tour.description like \'%hot spring%\' "
							+ "OR line_tour.description like \'%Hot Spring%\';");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result += (rs.getString(1) + " " + rs.getString(2) + "\n" + rs.getString(3) + "\n\n");
			}
			if (result.equals("")) {
				result = "No tours with hot spring! Thanks for your interest and support!\n";
			} else {
				String header = "The following are tours with hot spring.\n\n";
				result = header + result;
			}
			rs.close();
			stmt.close();
			connection.close();
			return result;
		} catch (Exception e) {
			log.info("Exception while reading database: {}", e.toString());
			return null;
		}
	}

	
	/**
	 * Returns tours which organize hiking activity
	 * 
	 * @return			tours which organize hiking activity
	 * @throws			Exception when database is not accessed successfully
	 */
	
	public String dynamic_mountain() {
		try {
			String result = "";
			Connection connection = super.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT line_tour.id, line_tour.name, line_tour.description " + "FROM line_tour "
							+ "WHERE line_tour.description like \'%mountain%\' "
							+ "OR line_tour.description like \'%Mountain%\';");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result += (rs.getString(1) + " " + rs.getString(2) + "\n" + rs.getString(3) + "\n\n");
			}
			if (result.equals("")) {
				result = "No tours with mountain! Thanks for your interest and support!\n";
			} else {
				String header = "The following are tours with mountain.\n\n";
				result = header + result;
			}
			rs.close();
			stmt.close();
			connection.close();
			return result;
		} catch (Exception e) {
			log.info("Exception while reading database: {}", e.toString());
			return null;
		}
	}


	private String FILENAME = "/static/faq.txt";

}

/**
 * This class is a container for faq entry which is loaded from faq database
 * @author Group 16 
 */
@Slf4j
class faqEntry {
	faqEntry(int questionID, String Question, String Answer, int hit) {
		this.questionID = questionID;
		this.Question = Question;
		this.Answer = Answer;
		this.hit = hit;
	}

	/**
	 * Constructor of faqEntry class
	 * 
	 * @param questionID		question id of faq entry
	 * @param Question		question of faq entry
	 * @param Answer			answer of faq entry
	 * @param Keyword		keyword of faq entry
	 * @param hit			hit number of faq entry
	 */
	faqEntry(int questionID, String Question, String Answer, String Keyword, int hit) {
		this.questionID = questionID;
		this.Question = Question;
		this.Answer = Answer;
		this.Keyword = Keyword;
		this.hit = hit;
	}

	public int questionID;
	public String Question;
	public String Answer;
	public String Keyword;
	public int hit;
}