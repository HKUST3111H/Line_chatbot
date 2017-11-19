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
	 * @param offeringID
	 * @param date
	 * @param hotel
	 * @param maxCapacity
	 * @param price
	 * @param duration
	 * @param quota
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
	 * @return offeringID
	 */
	// get functions
	public int getOfferingID() {
		return offeringID;
	}
	/**
	 * @return date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @return guideName
	 */
	public String getGuideName() {
		return guideName;
	}
	/**
	 * @return guideAccount
	 */
	public String getGuideAccount() {
		return guideAccount;
	}
	/**
	 * @return hotel
	 */
	public String getHotel() {
		return hotel;
	}
	/**
	 * @return maxCapacity
	 */
	public int getMaxCapacity() {
		return maxCapacity;
	}
	/**
	 * @return minCapacity
	 */
	public int getMinCapacity() {
		return minCapacity;
	}
	/**
	 * @return state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @return price
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @return duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @return quota
	 */
	public int getQuota() {
		return quota;
	}
	/**
	 * @param offeringID
	 */
	// set functions
	public void setOfferingID(int offeringID) {
		this.offeringID = offeringID;
	}
	/**
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @param guideName
	 */
	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	/**
	 * @param guideAccount
	 */
	public void setGuideAccount(String guideAccount) {
		this.guideAccount = guideAccount;
	}
	/**
	 * @param hotel
	 */
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}
	/**
	 * @param maxCapacity
	 */
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	/**
	 * @param minCapacity
	 */
	public void setMinCapacity(int minCapacity) {
		this.minCapacity = minCapacity;
	}

	/**
	 *OPEN = 0;
	 *CONFIRMED = 1;
	 *CANCLE = 2;
	 *CLOSED = 3;
	 * @param state
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * @param price
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @param quota
	 */
	public void setQuota(int quota) {
		this.quota = quota;
	}
}
