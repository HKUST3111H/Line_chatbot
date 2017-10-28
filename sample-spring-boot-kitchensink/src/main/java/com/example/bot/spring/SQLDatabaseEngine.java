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
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_faq;");
			ResultSet rs = stmt.executeQuery();
			while (result == null && rs.next()) {
				if (text.toLowerCase().contains(rs.getString(1).toLowerCase())) {
					result = rs.getString(2)+" //"+rs.getInt(3);
					PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_faq SET hit = ? WHERE question = ?;");
					int hits = rs.getInt(3)+1;
					String keyword=rs.getString(1);
					stmt2.setInt(1, hits);
					stmt2.setString(2, keyword);
					stmt2.executeQuery();
					stmt2.close();
				}
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
	
	void setUserTime(String id, java.sql.Timestamp time) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET last_login = ? WHERE id = ?;");
			stmt2.setTimestamp(1, time);
			stmt2.setString(2, id);
			stmt2.executeQuery();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (1==1)
			return ;
		throw new Exception("NOT FOUND");
	}
	
	void setUserState(String id, int FAQ1) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET state = ? WHERE id = ?;");
			stmt2.setInt(1, FAQ1);
			stmt2.setString(2, id);
			stmt2.executeQuery();
			stmt2.close();
			connection.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (1==1)
			return;
		throw new Exception("NOT FOUND");
	}
	
	void createUser(String id, java.sql.Timestamp time, int state) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		try {
			PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO line_user (id, state, last_login) VALUES (?, ?, ?);");
			stmt2.setString(1, id);
			stmt2.setInt(2, state);
			stmt2.setTimestamp(3, time);
			stmt2.executeQuery();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (1==1)
			return ;
		throw new Exception("NOT FOUND");
	}
}
