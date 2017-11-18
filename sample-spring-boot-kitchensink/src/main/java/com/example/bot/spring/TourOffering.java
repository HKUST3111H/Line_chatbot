package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TourOffering {

	private int offeringID;
	private String date;
	private String guideName;
	private String guideAccount;
	private String hotel;
	private int maxCapacity;
	private int minCapacity;
	private List<User> users;
	private int state;
	private int price;
	private int duration;
	private int quota;
	
	// constructor
	public TourOffering(int offeringID, String date,String hotel,int maxCapacity, int price, int duration, int quota) {
		this.offeringID = offeringID;
		this.date = date;
		this.hotel = hotel;
		this.maxCapacity = maxCapacity;
		this.price = price;
		this.duration = duration;
		this.quota = quota;
	}
	
	public void registerUser(User user) {
		if ( !users.contains(user) ) users.add(user);
	}
	
	public void unregisterUser(User user) {
		if ( users.contains(user) ) users.remove(user);
	}
	
	
	// get functions
	public int getOfferingID() {
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
	
	public int getState() {
		return state;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getQuota() {
		return quota;
	}
	
	// set functions
	public void setOfferingID(int offeringID) {
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
	
	public void setState(int state) {
		this.state = state;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setQuota(int quota) {
		this.quota = quota;
	}
}
