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

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		String result = null;
		int result2=0;
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT answer, hit FROM line_faq WHERE ? like concat('%',question,'%')");
			stmt.setString(1, text);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result=rs.getString(1);
				int hit =rs.getInt(2);
				hit=hit+1;
				PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_faq SET hit = ? WHERE answer = ?;");
				
				stmt2.setInt(1, hit);
				stmt2.setString(2, result);
				result2=stmt2.executeUpdate();
				stmt2.close();
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result == null)
			result ="null";
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
	
	User getUserInformation(String id) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		User result=new User();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_user WHERE id = ?;");
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
					result.setID(rs.getString(1));
					result.setName(rs.getString(2));
					result.setPhoneNumber(rs.getString(3));
					result.setAge(rs.getString(4));
					result.setState(rs.getInt(5));
					result.setTime(rs.getTimestamp(6));
					// train offering
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (1==1)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserTime(String id, java.sql.Timestamp time) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET last_login = ? WHERE id = ?;");
			stmt2.setTimestamp(1, time);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserState(String id, int FAQ1) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET state = ? WHERE id = ?;");
			stmt2.setInt(1, FAQ1);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserName(String id, String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET name = ? WHERE id = ?;");
			stmt2.setString(1, text);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserPhoneNum(String id, String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET phone_num = ? WHERE id = ?;");
			stmt2.setString(1, text);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserAge(String id, String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET age = ? WHERE id = ?;");
			stmt2.setString(1, text);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean createUser(String id, java.sql.Timestamp time, int state) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO line_user (id, state, last_login) VALUES (?, ?, ?);");
			stmt2.setString(1, id);
			stmt2.setInt(2, state);
			stmt2.setTimestamp(3, time);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean addToUnknownDatatabse(String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO line_unknownquestion (question) VALUES (?);");
			stmt2.setString(1, text);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	String getTourNames() throws Exception {
		//Write your code here
		Connection connection = getConnection();
		String result="";
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_tour;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result += (rs.getString(1)+" "+rs.getString(2)+"\n"+rs.getString(3)+"\n\n");
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result == "")
			result ="null";
		if (result != "")
			return result;
		throw new Exception("NOT FOUND");
	}
	
	boolean tourFound(String tourID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		boolean result=false;
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_tour WHERE id = ?;");
			stmt.setString(1, tourID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result=true;
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (1==1)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	String displayTourOffering(String tourID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		String result="";
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_touroffering;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result += (rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" max people:"+rs.getInt(5)+"\n\n");
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result == "")
			result ="null";
		if (result != "")
			return result;
		throw new Exception("NOT FOUND");
	}
	
	boolean setBookingTourID(String userID, String tourID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO line_userchoose (user_id, tour_id) VALUES (?, ?);");
			stmt2.setString(1, userID);
			stmt2.setString(2, tourID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	
}
