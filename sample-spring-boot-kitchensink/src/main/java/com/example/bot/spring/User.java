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
/**
 * This class is a container for user
 * @author Group 16
 */
@Slf4j
public class User {
	
	private String userName;
	private String userID;
	private String phoneNumber;
	private List<String> tripHistory;
	private String age;
	private int state;//chatting or booking state
	private java.sql.Timestamp time;
	
	public User (){//except for tripHistory
		tripHistory = new ArrayList<String>();
		this.userID = "null";
		this.userName = "null";
		this.phoneNumber = "null";
		this.age = "0";
		this.state = -1;	
	}
	
	/**
	 * Constructor
	 * @param id id of a user
	 * @param name name 
	 * @param phoneno phone numeber
	 * @param age age 
	 * @param state booking state
	 * @param time last log in time
	 */
	public User (String id,String name, String phoneno, String age, int state, java.sql.Timestamp time ){//except for tripHistory
		tripHistory = new ArrayList<String>();
		this.userID = id;
		this.userName = name;
		this.phoneNumber = phoneno;
		this.age = age;
		this.state = state;
		this.time = time;
	}

	
	//getter
	
	/**
	 * @return user name
	 */
	public String getUserName(){
		return userName;
	}
	
	/**
	 * @return user id 
	 */
	public String getUserID(){
		return userID;
	}
	
	/**
	 * @return phone number
	 */
	public String getPhoneNumber(){
		return phoneNumber;
	}
	
	/**
	 * @return age 
	 */
	public String getAge(){
		return age;
	}
	
	/**
	 * @return booking state 
	 */
	public int getState(){
		return state;
	}
	
	/**
	 * @return last log in time
	 */
	public java.sql.Timestamp getTime(){
		return time;
	}
	
	/**
	 * show tour history
	 */
	public void outputTripHistory(){
		for(int i = 0; i <this.tripHistory.size();i++){
			System.out.println(tripHistory.get(i));
		}
	}
	
	/**
	 * @param state booking state
	 */
	//mutator
	public void setState(int state){
		this.state = state; //chatting => 0 , booking => 1
	}
	
	/**
	 * @param Age age 
	 */
	public void setAge(String Age){
		this.age = Age;
	}
	
	/**
	 * @param PhoneNo phone number 
	 */
	public void setPhoneNumber(String PhoneNo){
		this.phoneNumber = PhoneNo;
	}
	
	/**
	 * @param userID user id 
	 */
	public void setID(String userID){
		this.userID = userID;
	}
	
	/**
	 * @param userName name of user
	 */
	public void setName(String userName){
		this.userName = userName;
	}
	
	/**
	 * @param tourID id of tour
	 */
	public void addTripHistory(String tourID){
		this.tripHistory.add(tourID);
		
	}
	
	/**
	 * @param time last log in time
	 */
	public void setTime(java.sql.Timestamp time){
		this.time = time;
	}
	
	/**
	 * @param userID id of user
	 * @param time last log in time
	 * @param state booking state
	 */
	public void setUser(String userID, java.sql.Timestamp time, int state) {
		setID(userID);
		setTime(time);
		setState(state);
	}
	

}
