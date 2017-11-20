package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for tour offerings
 * @author Group 16
 */
@Slf4j
public class TourOffering {

	private int offeringID;
	private String date;
	private String guideName;
	private String guideAccount;
	private String hotel;
	private int maxCapacity;
	private int minCapacity;
	private int state;
	private int price;
	private int duration;
	private int quota;
	
	// constructor
	/**
	 * Constructor
	 * @param offeringID ID of tour offering
	 * @param date date of tour offering
	 * @param hotel hotel of tour offering 
	 * @param maxCapacity max capacity of tour offering
	 * @param price price of tour offering
	 * @param duration duration of tour offering
	 * @param quota quota of tour offering
	 */
	public TourOffering(int offeringID, String date,String hotel,int maxCapacity, int price, int duration, int quota) {
		this.offeringID = offeringID;
		this.date = date;
		this.hotel = hotel;
		this.maxCapacity = maxCapacity;
		this.price = price;
		this.duration = duration;
		this.quota = quota;
	}
	
	/**
	 * Constructor
	 * @param offeringID ID of tour offering
	 * @param date date of tour offering
	 * @param hotel hotel of tour offering
	 * @param maxCapacity max capacity of tour offering
	 * @param price price of tour offering
	 * @param duration duration of tour offering
	 * @param quota quota of tour offering
	 * @param guideName name of the guide 
	 * @param guideAccount line account of the guide 
	 * @param minCapacity minimum capacity of tour offering
	 * @param state	state of tour offering
	 */
	public TourOffering(int offeringID, String date,String hotel,int maxCapacity, int price, int duration, int quota,
			String guideName,String guideAccount,int minCapacity,int state) {
		this.offeringID = offeringID;
		this.date = date;
		this.hotel = hotel;
		this.maxCapacity = maxCapacity;
		this.price = price;
		this.duration = duration;
		this.quota = quota;
		this.guideName = guideName;
		this.guideAccount = guideAccount;
		this.minCapacity = minCapacity;
		this.state = state;
	}
	// get functions
	/**
	 * Gets ID of the tour offering
	 * @return ID of the tour offering
	 */
	public int getOfferingID() {
		return offeringID;
	}
	/**
	 * Gets date of the tour offering
	 * @return date of the tour offering
	 */
	public String getDate() {
		return date;
	}
	/**
	 * Gets name of the tour guide
	 * @return name of the tour guide
	 */
	public String getGuideName() {
		return guideName;
	}
	/**
	 * Gets line account of the tour guide
	 * @return line account of the tour guide
	 */
	public String getGuideAccount() {
		return guideAccount;
	}
	/**
	 * Gets name of the hotel
	 * @return name of the hotel 
	 */
	public String getHotel() {
		return hotel;
	}
	/**
	 * Gets the maximum capacity of a tour offering
	 * @return maximum capacity of a tour offering
	 */
	public int getMaxCapacity() {
		return maxCapacity;
	}
	/**
	 * Gets the minimum capacity of a tour offering
	 * @return minimum capacity of a tour offering
	 */
	public int getMinCapacity() {
		return minCapacity;
	}
	/**
	 * Gets the state of the tour offering
	 * @return state of the tour offering
	 */
	public int getState() {
		return state;
	}

	/**
	 * Gets the price of the tour offering
	 * @return the price of the tour offering
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * Gets the duration of the tour offering
	 * @return duration of the tour offering
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * Gets the quota of the tour offering
	 * @return quota of the tour offering
	 */
	public int getQuota() {
		return quota;
	}
	
	// set functions
	/**
	 * Sets the offering ID
	 * @param offeringID ID of the tour offering to be set
	 */
	public void setOfferingID(int offeringID) {
		this.offeringID = offeringID;
	}
	/**
	 * Sets the data of the tour offering
	 * @param date date of the tour offering to be set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * Sets the name of the tour guide
	 * @param guideName name of the guide to be set
	 */
	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	/**
	 * Sets the line account of the tour guide
	 * @param guideAccount line account of the tour guide to be set
	 */
	public void setGuideAccount(String guideAccount) {
		this.guideAccount = guideAccount;
	}
	/**
	 * Sets the hotel name of the tour offering
	 * @param hotel hotel name of the tour offering to be set
	 */
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}
	/**
	 * Sets the max capacity of the tour offering
	 * @param maxCapacity max capacity of the tour offering to be set
	 */
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	/**
	 * Sets the min capacity of the tour offering
	 * @param minCapacity min capacity of the tour offering to be set
	 */
	public void setMinCapacity(int minCapacity) {
		this.minCapacity = minCapacity;
	}

	/**
	 * Sets the state of the tour offering
	 * OPEN = 0;
	 * CONFIRMED = 1;
	 * CANCLE = 2;
	 * CLOSED = 3;
	 * @param state state of the tour offering to be set
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * Sets the price of the tour offering
	 * @param price price of the tour offering to be set
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * Sets the duration of the tour offering
	 * @param duration duration of the tour offering to be set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * Sets the quota of the tour offering
	 * @param quota quota of the tour offering to be set
	 */
	public void setQuota(int quota) {
		this.quota = quota;
	}
}
