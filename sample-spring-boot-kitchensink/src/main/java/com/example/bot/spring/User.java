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
 * @author Group 16
 * This class is a container for Users
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
	 * Constructor
	 * @param userID
	 * @param userName
	 * @param phoneNumber
	 * @param age
	 * @param state
	 * @param time
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
	/**
	 *Default Constructorbuild
	 */
	public User (){//except for tripHistory
		tripHistory = new ArrayList<String>();
		this.userID = "null";
		this.userName = "null";
		this.phoneNumber = "null";
		this.age = "0";
		this.state = -1;	
	}
	
	//getter
	/**
	 * @return userName
	 */
	String getUserName(){
		return this.userName;
	}
	/**
	 * @return userID
	 */
	String getUserID(){
		return this.userID;
	}
	/**
	 * @return phoneNumber
	 */
	String getPhoneNumber(){
		return this.phoneNumber;
	}
	/**
	 * @return age
	 */
	String getAge(){
		return this.age;
	}
	/**
	 * @return state
	 */
	int getState(){
		return this.state;
	}
	/**
	 * @return time
	 */
	java.sql.Timestamp getTime(){
		return this.time;
	}
	/**
	 * show tour history
	 */
	void outputTripHistory(){
		for(int i = 0; i <this.tripHistory.size();i++){
			System.out.println(tripHistory.get(i));
		}
	}
	/**
	 *FAQ_NO_USER_INFORMATION = 100;
	 *FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION = 300;
	 *FAQ_AFTER_CONFIRMATION = 500;
	 *
	 * tell user to fill in his personal information
	 *FILL_NAME = 200;
	 *FILL_PHONE_NUM = 201; 
	 *FILL_AGE = 202; 
	 *
	 * tell user to provide booking information
	 *BOOKING_TOUR_ID = 400;
	 *BOOKING_OFFERING_ID = 401;
	 *BOOKING_ADULT = 402;
	 *BOOKING_CHILDREN = 403;
	 *BOOKING_TODDLER = 404;
	 *BOOKING_CONFIRMATION = 405;
	 *BOOKING_PAYMENT = 406;
	 *
	 * tell user to add a new booking or review
	 *BOOKING_OR_REVIEW = 600;
	 * @param state
	 */
	//mutator
	void setState(int state){
		this.state = state; //chatting => 0 , booking => 1
	}
	/**
	 * @param Age
	 */
	void setAge(String Age){
		this.age = Age;
	}
	/**
	 * @param PhoneNo
	 */
	void setPhoneNumber(String PhoneNo){
		this.phoneNumber = PhoneNo;
	}
	/**
	 * @param userID
	 */
	void setID(String userID){
		this.userID = userID;
	}
	/**
	 * @param userName
	 */
	void setName(String userName){
		this.userName = userName;
	}
	/**
	 * @param tourID
	 */
	void addTripHistory(String tourID){
		this.tripHistory.add(tourID);
		
	}
	/**
	 * @param time
	 */
	void setTime(java.sql.Timestamp time){
		this.time = time;
	}
	/**
	 * @param userID
	 * @param time
	 * @param time
	 */
	void setUser(String userID, java.sql.Timestamp time, int state) {
		setID(userID);
		setTime(time);
		setState(state);
	}
	

}
