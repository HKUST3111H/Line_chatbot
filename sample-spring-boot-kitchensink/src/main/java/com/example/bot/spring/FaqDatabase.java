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
		Connection connection = getConnection();
		List<faqEntry> listOfEntry = new ArrayList<faqEntry>();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_faq;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
		    	faqEntry entry=new faqEntry(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4));
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
	      
	      
		

	
	public String search(String text)throws Exception{

		
		
		String result = null;
		
		List<faqEntry> listOfEntry=loadQuestion();
		
		
		// using wagnerFischer Algorithm to select question within 10 unit distance
		if (!listOfEntry.isEmpty()) {
			int dist;
			int minDistance=1000000;
			for (faqEntry entry:listOfEntry) {
				dist=new WagnerFischer(entry.Question,text).getDistance();
				if ( dist<=10 && dist<minDistance) {
					minDistance=dist;
					result=entry.Answer;
				}
			}
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


class faqEntry{
	faqEntry(int questionID, String Question, String Answer, int hit){
		this.questionID=questionID;
		this.Question=Question;
		this.Answer=Answer;
		this.hit=hit;
	}
	public int questionID;
	public String Question;
	public String Answer;
	public int hit;
}