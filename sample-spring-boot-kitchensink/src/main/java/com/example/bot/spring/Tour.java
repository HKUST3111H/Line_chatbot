package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for tours
 * @author Group 16
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
	 * @param tourID ID number of a tour
	 * @param tourName name of the tour
	 * @param description description of the tour
	 * @param duration duration of the tour
	 * @param imagePath path of the image
	 * @param shortDescription a short version of description
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
	 * @param tourID ID number of a tour
	 * @param tourName name of the tour
	 * @param description description of the tour
	 * @param duration duration of the tour
	 */
	public Tour(int tourID, String tourName, String description, int duration) {
		this.tourID = tourID;
		this.tourName = tourName;
		this.description = description;
		this.duration = duration;
	}
	
	
	
	// get functions
	/**
	 * Gets tour ID
	 * @return id number of a tour
	 */
	public int getTourID() {
		return tourID;
	}
	
	/**
	 * Gets tour name
	 * @return name of the tour
	 */
	public String getTourName() {
		return tourName;
	}
	
	/**
	 * Gets tour description
	 * @return description of the tour 
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets tour duration
	 * @return duration of the tour
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * Gets image path
	 * @return path of an image
	 */
	public String getImagePath() {
		return imagePath;
	}
	/**
	 * Gets short version of tour description
	 * @return short version of description
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	// set functions
	/**
	 * Sets tour ID
	 * @param tourID ID number of the tour to be set
	 */
	public void setTourID(int tourID) {
		this.tourID = tourID;
	}
	
	/**
	 * Sets tour name
	 * @param tourName name of the tour to be set
	 */
	public void setTourName(String tourName) {
		this.tourName = tourName;
	}
	
	/**
	 * Sets tour description
	 * @param description description of the tour to be set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Sets tour duration
	 * @param duration duration of the tour to be set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * Sets image path
	 * @param imagePath path of the image to be set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	/**
	 * Sets a short version of description
	 * @param shortDescription a short version of description to be set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	
}
