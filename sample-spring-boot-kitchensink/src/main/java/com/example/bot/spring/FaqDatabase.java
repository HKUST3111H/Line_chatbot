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

@Slf4j
public class FaqDatabase extends SQLDatabaseEngine {
	
	
	public String replyImage(String answer) {
        //String pattern = "(\\d+)[.](.*[?])[\\n][>](.*)[\\n]";
        String pattern = "see the picture (.*)[)]";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(answer);
	    if (m.find()) {
	    	return m.group(1);
	    }
	    else return null;
	}
	
	private List<faqEntry> loadQuestion() throws Exception{
		//return loadQuestionStatic();
		return loadQuestionSQL();
		
	}
	
	private List<faqEntry> loadQuestionSQL() throws Exception{
		Connection connection = super.getConnection();
		List<faqEntry> listOfEntry = new ArrayList<faqEntry>();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT line_faq.id, line_faq.question, line_faq.answer, line_faq.hit, line_keyword.keyword_text "
					+ "FROM line_faq, line_keyword, line_faq_keyword WHERE line_faq.id=line_faq_keyword.faq_id AND "
					+ "line_keyword.id=line_faq_keyword.keyword_id;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
		    	faqEntry entry=new faqEntry(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(5),rs.getInt(4));
		    	listOfEntry.add(entry);
					//PreparedStatement stmt2 = connection.prepareStatement("UPDATE keywords SET hit = ? WHERE question = ?;");
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			connection.close();		
		}
		if (listOfEntry != null && !listOfEntry.isEmpty())
			return listOfEntry;
		log.info("faq database probably empty");
		throw new Exception("EMPTY DATABASE");
	}
	
	private boolean updateHit(int qid, int hit) throws Exception{
		Connection connection = super.getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_faq SET hit = ? WHERE id = ?;");
			stmt2.setInt(1, hit);
			stmt2.setInt(2, qid);
			result=stmt2.executeUpdate();
			stmt2.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			connection.close();		
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("fail");
	}
	
	
	private List<faqEntry> loadQuestionStatic() throws Exception{
		
		List<faqEntry> listOfEntry = new ArrayList<faqEntry>();
		
//load from static file and process by re
		BufferedReader br = null;
		InputStreamReader isr = null;
		String para = null;
		try {
			isr = new InputStreamReader(
                    this.getClass().getResourceAsStream(FILENAME));
			br = new BufferedReader(isr);
			for (String line; (line = br.readLine()) != null; para += (line+"\n"));			
		} catch (IOException e) {
			log.info("IOException while reading file: {}", e.toString());
		} finally {
			try {
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
			} catch (IOException ex) {
				log.info("IOException while closing file: {}", ex.toString());
			}
		}
//loaded text from static file		
		if (para != null) {
	        String pattern = "(\\d+)[.](.*[?])[\\n][>](.*)[\\n]";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(para);
		    while(m.find()) {
		    	faqEntry entry=new faqEntry(Integer.parseInt(m.group(1)),m.group(2),m.group(3),0);
		    	listOfEntry.add(entry);
		    }
		      return listOfEntry;
		}
		throw new Exception("Cannot load faq from database");
		
	}
	      
	      
		

	
	public String search(String text, String userID)throws Exception{
		
		String result = null;
		
		List<faqEntry> listOfEntry=loadQuestion();
		int qid=-1;
		int hit=0;
		// using wagnerFischer Algorithm to select question within 10 unit distance
		if (!listOfEntry.isEmpty()) {
			int dist;
			int minDistance=1000000;
			for (faqEntry entry:listOfEntry) {
				dist=new WagnerFischer(entry.Question,text).getDistance();
				if ( dist<=2 && dist<minDistance) {
					minDistance=dist;
					result=entry.Answer;
					qid=entry.questionID;
					hit=entry.hit;
				}
			}
			if (result == null) {
				for (faqEntry entry:listOfEntry) {
					if (text.toLowerCase().contains(entry.Keyword.toLowerCase())) {
						dist=new WagnerFischer(entry.Question,text).getDistance();
						if (dist<=30 && dist<minDistance) {
							minDistance=dist;
							result=entry.Answer;
							qid=entry.questionID;
							hit=entry.hit;
						}
					}
				}
			}
			
			// dynamic question
			if(result.toLowerCase().contains("return")) {
				Connection connection = super.getConnection();
				if(result.toLowerCase().contains("top 5 tours")) {
					try {
						result = "";
						PreparedStatement stmt = connection.prepareStatement(
								"SELECT line_tour.id, line_tour.name, line_tour.description, COUNT(line_booking.state) "
								+ "FROM line_booking, line_touroffering, line_tour "
								+ "WHERE line_booking.\"tourOffering_id\"=line_touroffering.id AND line_touroffering.tour_id=line_tour.id "
								+ "AND line_booking.state > 0 "
								+ "AND line_tour.id NOT IN "
								+ "(SELECT line_tour.id FROM line_booking, line_touroffering, line_tour "
								+ "WHERE line_booking.user_id = ? AND line_booking.state = 2 AND "
								+ "line_booking.\"tourOffering_id\"=line_touroffering.id AND line_touroffering.tour_id=line_tour.id) "
								+ "GROUP BY line_tour.id, line_tour.name, line_tour.description "
								+ "ORDER BY COUNT(line_booking.state) DESC;");
						stmt.setString(1, userID);
						ResultSet rs = stmt.executeQuery();
						int i = 0;
						while (rs.next() && i<5) {							
							result += (rs.getString(1)+" "+rs.getString(2)+"\n"+rs.getString(3)+"\n"+
						"Number of people who have visited: "+ rs.getInt(4)+"\n\n");
							i++;
						}
						if (result.equals("")){
							result = "You have been to all tours we provided. Thanks for your interest and support!\n";
						}
						else {
							String header = "The following are hot tours we recommend based on your booking history.\n\n";
							result = header+result;
						}
						rs.close();
						stmt.close();
						connection.close();
					}
					catch (Exception e) {
						log.info(e.toString());
					} finally {

					}
					
				}
				else if(result.toLowerCase().contains("more tours")) {
					try {
						result = "";
						PreparedStatement stmt = connection.prepareStatement(
								"SELECT line_tour.id, line_tour.name, line_tour.description, COUNT(line_booking.state) "
								+ "FROM line_booking, line_touroffering, line_tour "
								+ "WHERE line_booking.\"tourOffering_id\"=line_touroffering.id AND line_touroffering.tour_id=line_tour.id "
								+ "AND line_booking.state > 0 "
								+ "AND line_tour.id NOT IN "
								+ "(SELECT line_tour.id FROM line_booking, line_touroffering, line_tour "
								+ "WHERE line_booking.user_id = ? AND line_booking.state = 2 AND "
								+ "line_booking.\"tourOffering_id\"=line_touroffering.id AND line_touroffering.tour_id=line_tour.id) "
								+ "GROUP BY line_tour.id, line_tour.name, line_tour.description "
								+ "ORDER BY COUNT(line_booking.state) DESC;");
						stmt.setString(1, userID);
						ResultSet rs = stmt.executeQuery();
						ResultSet temp = rs;
						int i = 0;
						while (i<5 && rs.next()) {							
							i++;
						}
						while(rs.next()) {
							result += (rs.getString(1)+" "+rs.getString(2)+"\n"+rs.getString(3)+"\n"+
						"Number of people who have visited: "+ rs.getInt(4)+"\n\n");
						}
						if (result.equals("")){
							result = "No more recommendations! Thanks for your interest and support!\n";
						}
						else {
							String header = "The following are more hot tours we recommend based on your booking history.\n\n";
							result = header+result;
						}
						rs.close();
						stmt.close();
						connection.close();
					}
					catch (Exception e) {
						log.info(e.toString());
					} finally {

					}
					
				}
			}
		}
		if(qid!=-1) {
			updateHit(qid, hit+1);
		}
		
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}

	/*
	public void setFilename(String txt) {
		this.FILENAME=txt;
	}
	*/
	private String FILENAME = "/static/faq.txt";
	
}

@Slf4j
class faqEntry{
	faqEntry(int questionID, String Question, String Answer, int hit){
		this.questionID=questionID;
		this.Question=Question;
		this.Answer=Answer;
		this.hit=hit;
	}
	faqEntry(int questionID, String Question, String Answer, String Keyword, int hit){
		this.questionID=questionID;
		this.Question=Question;
		this.Answer=Answer;
		this.Keyword=Keyword;
		this.hit=hit;
	}
	public int questionID;
	public String Question;
	public String Answer;
	public String Keyword;
	public int hit;
}