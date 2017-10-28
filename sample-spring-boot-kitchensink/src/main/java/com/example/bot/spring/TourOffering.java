package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TourOffering {

	private String offeringID;
	private String date;
	private String guideName;
	private String guideAccount;
	private String hotel;
	private int maxCapacity;
	private int minCapacity;
	private List<User> users;
	
	// constructor
	public TourOffering(String offeringID, String date, String guideName, String guideAccount,
			String hotel, int maxCapacity, int minCapacity) {
		this.offeringID = offeringID;
		this.date = date;
		this.guideName = guideName;
		this.guideAccount = guideAccount;
		this.hotel = hotel;
		this.maxCapacity = maxCapacity;
		this.minCapacity = minCapacity;
		this.users = new ArrayList<User>();
	}
	
	
	
	public void registerUser(User user) {
		if ( !users.contains(user) ) users.add(user);
	}
	
	public void unregisterUser(User user) {
		if ( users.contains(user) ) users.remove(user);
	}
	
	
	// get functions
	public String getOfferingID() {
		return offeringID;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getGuideName() {
		return guideName;
	}
	
	public String getGuideAccount() {
		return guideAccount;
	}
	
	public String getHotel() {
		return hotel;
	}
	
	public int getMaxCapacity() {
		return maxCapacity;
	}
	
	public int getMinCapacity() {
		return minCapacity;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	// set functions
	public void setOfferingID(String offeringID) {
		this.offeringID = offeringID;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	
	public void setGuideAccount(String guideAccount) {
		this.guideAccount = guideAccount;
	}
	
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}
	
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	
	public void setMinCapacity(int minCapacity) {
		this.minCapacity = minCapacity;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
