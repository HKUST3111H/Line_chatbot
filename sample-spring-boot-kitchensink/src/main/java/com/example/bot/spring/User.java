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
	
	
	/**
	 * Default constructor
	 */
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
	 * @param id id of the line user
	 * @param name name of the user
	 * @param phoneno phone numeber of the user
	 * @param age age of the user
	 * @param state booking state of the user
	 * @param time last log in time of the user
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
	 * Gets the user name
	 * @return name of the user
	 */
	public String getUserName(){
		return userName;
	}
	
	/**
	 * Gets the user ID
	 * @return ID of the user
	 */
	public String getUserID(){
		return userID;
	}
	
	/**
	 * Gets the phone number of the user
	 * @return phone number of the user
	 */
	public String getPhoneNumber(){
		return phoneNumber;
	}
	
	/**
	 * Gets the age of the user
	 * @return age of the user
	 */
	public String getAge(){
		return age;
	}
	
	/**
	 * Gets the state of the user
	 * @return state of the user
	 */
	public int getState(){
		return state;
	}
	
	/**
	 * Gets last log in time of the user
	 * @return last log in time of the user
	 */
	public java.sql.Timestamp getTime(){
		return time;
	}
	
	/**
	 * Shows tour history of the user
	 */
	public void outputTripHistory(){
		for(int i = 0; i <this.tripHistory.size();i++){
			System.out.println(tripHistory.get(i));
		}
	}
	
	/**
	 * Sets the state of the user
	 * @param state state of the user to be set
	 */
	//mutator
	public void setState(int state){
		this.state = state; //chatting => 0 , booking => 1
	}
	
	/**
	 * Sets the age of the user
	 * @param Age age of the user to be set
	 */
	public void setAge(String Age){
		this.age = Age;
	}
	
	/**
	 * Sets the phone number of the user
	 * @param PhoneNo phone number of the user to be set 
	 */
	public void setPhoneNumber(String PhoneNo){
		this.phoneNumber = PhoneNo;
	}
	
	/**
	 * Sets the ID of the user
	 * @param userID ID of the user to be set
	 */
	public void setID(String userID){
		this.userID = userID;
	}
	
	/**
	 * Sets the name of the user
	 * @param userName name of the user to be set
	 */
	public void setName(String userName){
		this.userName = userName;
	}
	
	/**
	 * Adds a tour to the trip history of the user
	 * @param tourID ID of the tour to be added
	 */
	public void addTripHistory(String tourID){
		this.tripHistory.add(tourID);
		
	}
	
	/**
	 * Sets last log in time of the user
	 * @param time last log in time of the user to be set
	 */
	public void setTime(java.sql.Timestamp time){
		this.time = time;
	}
	
	/**
	 * Sets information of the user
	 * @param userID ID of the user to be set
	 * @param time last log in time of the user to be set
	 * @param state state of the user to be set
	 */
	public void setUser(String userID, java.sql.Timestamp time, int state) {
		setID(userID);
		setTime(time);
		setState(state);
	}
	

}
