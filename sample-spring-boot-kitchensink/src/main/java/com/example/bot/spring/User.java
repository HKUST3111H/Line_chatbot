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

import java.util.ArrayList;

import java.util.List;
@Slf4j
public class User {
	private String userName;
	private String userID;
	private String phoneNumber;
	private List<String> tripHistory;
	private String age;
	private int state;//chatting or booking state
	
	public User (String id,String name, String phoneno, String age, int state ){//except for tripHistory
		tripHistory = new ArrayList<String>();
		this.userID = id;
		this.userName = name;
		this.phoneNumber = phoneno;
		this.age = age;
		this.state = state;	
	}
	public User (){//except for tripHistory
		tripHistory = new ArrayList<String>();
		this.userID = "null";
		this.userName = "null";
		this.phoneNumber = "null";
		this.age = "0";
		this.state = -1;	
	}
	
	//getter
	String getUserName(){
		return this.userName;
	}
	String getUserID(){
		return this.userID;
	}
	String getPhoneNumber(){
		return this.phoneNumber;
	}
	String getAge(){
		return this.age;
	}
	int getState(){
		return this.state;
	}
	void outputTripHistory(){
		for(int i = 0; i <this.tripHistory.size();i++){
			System.out.println(tripHistory.get(i));
		}
	}
	
	//mutator
	void setState(int state){
		this.state = state; //chatting => 0 , booking => 1
	}

	void setAge(String Age){
		this.age = Age;
	}
	void setPhoneNumber(String PhoneNo){
		this.phoneNumber = PhoneNo;
	}
	void setID(String userID){
		this.userID = userID;
	}
	void setName(String userName){
		this.userName = userName;
	}
	void addTripHistory(String tripID){
		this.tripHistory.add(tripID);
		
	}
	

}
