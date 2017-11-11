package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Tour {
	
	private int tourID;
	private String tourName;
	private String description;
	private int duration;
	private List<TourOffering> offerings;
	
	
	// constructor
	public Tour(int tourID, String tourName, String description, int duration) {
		this.tourID = tourID;
		this.tourName = tourName;
		this.description = description;
		this.duration = duration;
		this.offerings = new ArrayList<TourOffering>();
	}
	
	public void addTourOffering(TourOffering offering) {
		if ( !offerings.contains(offering) ) this.offerings.add(offering);
	}
	
	public void removeTourOffering(TourOffering offering) {
		if ( offerings.contains(offering) ) this.offerings.remove(offering);
	}
	
	
	
	// get functions
	public int getTourID() {
		return tourID;
	}
	
	public String getTourName() {
		return tourName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public List<TourOffering> getOfferings(){
		return offerings;
	}
	
	// set functions
	public void setTourID(int tourID) {
		this.tourID = tourID;
	}
	
	public void setTourName(String tourName) {
		this.tourName = tourName;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setOfferings(List<TourOffering> offerings){
		this.offerings = offerings;
	}
	
	
}
