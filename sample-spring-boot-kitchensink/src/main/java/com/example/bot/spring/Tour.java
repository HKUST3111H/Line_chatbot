package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for Tours
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
	 * @param tourID Id number of a tour
	 * @param tourName Name of the tour
	 * @param description description of the tour
	 * @param duration duration of the tour
	 * @param imagePath path of the image
	 * @param shortDescription an short version of description
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
	 * @param tourID Id number of a tour
	 * @param tourName Name of the tour
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
	 * @return tourID id number of a tour
	 */
	public int getTourID() {
		return tourID;
	}
	
	/**
	 * @return tourName name of the tour
	 */
	public String getTourName() {
		return tourName;
	}
	
	/**
	 * @return description description of the tour 
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return duration duration of the tour
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @return imagePath path of an image
	 */
	public String getImagePath() {
		return imagePath;
	}
	/**
	 * @return shortDescription short version of description
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	// set functions
	/**
	 * @param tourID Id number of a tour
	 */
	public void setTourID(int tourID) {
		this.tourID = tourID;
	}
	
	/**
	 * @param tourName Name of the tour
	 */
	public void setTourName(String tourName) {
		this.tourName = tourName;
	}
	
	/**
	 * @param description description of the tour
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @param duration duration of the tour
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @param imagePath path of the image
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	/**
	 * @param shortDescription an short version of description
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	
}
