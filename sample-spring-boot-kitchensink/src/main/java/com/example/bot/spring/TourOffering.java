package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group 16
 * This class is a container for TourOfferings
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
	 * @param offeringID offeringID
	 * @param date date of tour offering
	 * @param hotel hotel of tour offering 
	 * @param maxCapacity max capacity of tour offering
	 * @param price price of of tour offering
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
	 * @param offeringID offeringID
	 * @param date date 
	 * @param hotel hotel 
	 * @param maxCapacity max capacity 
	 * @param price price 
	 * @param duration duration
	 * @param quota quota 
	 * @param guideName name of the guide 
	 * @param guideAccount account of the guide 
	 * @param minCapacity minimum capacity 
	 * @param state
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
	/**
	 * @return offeringID offeringID
	 */
	// get functions
	public int getOfferingID() {
		return offeringID;
	}
	/**
	 * @return date date 
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @return guideName name of the guide
	 */
	public String getGuideName() {
		return guideName;
	}
	/**
	 * @return guideAccount account of the guide
	 */
	public String getGuideAccount() {
		return guideAccount;
	}
	/**
	 * @return hotel hotel 
	 */
	public String getHotel() {
		return hotel;
	}
	/**
	 * @return maxCapacity max capacity
	 */
	public int getMaxCapacity() {
		return maxCapacity;
	}
	/**
	 * @return minCapacity min capacity
	 */
	public int getMinCapacity() {
		return minCapacity;
	}
	/**
	 * @return state state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @return price price
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @return duration duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @return quota quota 
	 */
	public int getQuota() {
		return quota;
	}
	/**
	 * @param offeringID offeringID
	 */
	// set functions
	public void setOfferingID(int offeringID) {
		this.offeringID = offeringID;
	}
	/**
	 * @param date date
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @param guideName guideName
	 */
	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	/**
	 * @param guideAccount account of the guide 
	 */
	public void setGuideAccount(String guideAccount) {
		this.guideAccount = guideAccount;
	}
	/**
	 * @param hotel hotel
	 */
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}
	/**
	 * @param maxCapacity max capacity
	 */
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	/**
	 * @param minCapacity minimum capacity
	 */
	public void setMinCapacity(int minCapacity) {
		this.minCapacity = minCapacity;
	}

	/**
	 *OPEN = 0;
	 *CONFIRMED = 1;
	 *CANCLE = 2;
	 *CLOSED = 3;
	 * @param state state
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * @param price price
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * @param duration duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @param quota quota
	 */
	public void setQuota(int quota) {
		this.quota = quota;
	}
}
