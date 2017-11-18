package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group 16
 * This class is a container for Tours
 */
@Slf4j
public class Tour {
	
	private int tourID;
	private String tourName;
	private String description;
	private int duration;
	private List<TourOffering> offerings;
	
	
	// constructor
	
	/**
	 * Constructor
	 * @param tourID
	 * @param tourName
	 * @param description
	 * @param duration
	 */
	public Tour(int tourID, String tourName, String description, int duration) {
		this.tourID = tourID;
		this.tourName = tourName;
		this.description = description;
		this.duration = duration;
		this.offerings = new ArrayList<TourOffering>();
	}
	
	
	/**
	 * @param offering
	 */
	public void addTourOffering(TourOffering offering) {
		if ( !offerings.contains(offering) ) this.offerings.add(offering);
	}
	
	/**
	 * @param offering
	 */
	public void removeTourOffering(TourOffering offering) {
		if ( offerings.contains(offering) ) this.offerings.remove(offering);
	}
	
	
	
	// get functions
	/**
	 * @return tourID
	 */
	public int getTourID() {
		return tourID;
	}
	
	/**
	 * @return tourName
	 */
	public String getTourName() {
		return tourName;
	}
	
	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return duration
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * @return a list of tour offerings
	 * @see TourOffering
	 */
	public List<TourOffering> getOfferings(){
		return offerings;
	}
	
	// set functions
	/**
	 * @param tourID
	 */
	public void setTourID(int tourID) {
		this.tourID = tourID;
	}
	
	/**
	 * @param tourName
	 */
	public void setTourName(String tourName) {
		this.tourName = tourName;
	}
	
	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * @param offerings list
	 * @see TourOffering
	 */
	public void setOfferings(List<TourOffering> offerings){
		this.offerings = offerings;
	}
	
	
}
