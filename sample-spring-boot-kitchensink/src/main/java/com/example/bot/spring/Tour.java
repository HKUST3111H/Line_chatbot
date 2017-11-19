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
	private String imagePath;
	private String shortDescription;
	
	// constructor
	
	/**
	 * Constructor
	 * @param tourID
	 * @param tourName
	 * @param description
	 * @param duration
	 * @param imagePath
	 * @param shortDescription
	 */
	public Tour(int tourID, String tourName, String description, int duration,String imagePath, String shortDescription) {
		this.tourID = tourID;
		this.tourName = tourName;
		this.description = description;
		this.duration = duration;
		this.imagePath = imagePath;
		this.shortDescription = shortDescription;
	}
	
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
	 * @return imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}
	/**
	 * @return shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
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
	 * @param imagePath
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	/**
	 * @param shortDescription
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	
}
