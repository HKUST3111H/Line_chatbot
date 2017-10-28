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
public class FaqDatabase {
	
	List<faqEntry> loadQuestion() throws Exception{
		
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
		    	faqEntry entry=new faqEntry(Integer.parseInt(m.group(1)),m.group(2),m.group(3));
		    	listOfEntry.add(entry);
		    }
		      return listOfEntry;
		}
		throw new Exception("Cannot load faq from database");
		
	}
	      
	      
		

	
	String search(String text)throws Exception{

		
		
		String result = null;
		
		List<faqEntry> listOfEntry=loadQuestion();
		if (listOfEntry!=null) {
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
	faqEntry(int questionID, String Question, String Answer){
		this.questionID=questionID;
		this.Question=Question;
		this.Answer=Answer;
	}
	public int questionID;
	public String Question;
	public String Answer;
}