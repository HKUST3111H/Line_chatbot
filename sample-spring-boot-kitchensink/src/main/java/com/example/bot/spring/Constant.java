package com.example.bot.spring;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

@Slf4j
public  class Constant {
	
	// FAQ
	public static final int FAQ_NO_USER_INFORMATION = 100;
	public static final int FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION = 300;
	public static final int FAQ_AFTER_CONFIRMATION = 500;
	
	// tell user to fill in his personal information
	public static final int FILL_NAME = 200;
	public static final int FILL_PHONE_NUM = 201; 
	public static final int FILL_AGE = 202; 
	
	// tell user to provide booking information
	public static final int BOOKING_TOUR_ID = 400;
	public static final int BOOKING_OFFERING_ID = 401;
	public static final int BOOKING_ADULT = 402;
	public static final int BOOKING_CHILDREN = 403;
	public static final int BOOKING_TODDLER = 404;
	public static final int BOOKING_CONFIRMATION = 405;
	public static final int BOOKING_PAYMENT = 406;
	
	// tell user to add a new booking or review
	public static final int BOOKING_OR_REVIEW = 600;
	
	public static final String GREETING_FIRST_USE = "Thanks for your first use of our app! "
			+ "You can ask us some questions about touring information "
			+ "and text \"book\" to book the tour offering which you are interested in!"
			+ "\n\n"; 
	public static final String MORNING = "Good morning";
	public static final String AFTERNOON = "Good afternoon";
	public static final String EVENING = "Good evening";
	public static final String WELCOME_BACK = "!\nWelcome back!\n\n";
	public static final String CANCEL = "Booking cancelled successfully!";
	public static final String INSTRUCTION_FILL_INFORMATION = "Thank you for your interest, "
			+ "we need some of your information.\n";
	public static final String INSTRUCTION_ENTER_NAME = "Please enter your name.\n";
	public static final String INSTRUCTION_ENTER_PHONE_NUM = "Please also give us your phone number.\n";
	public static final String INSTRUCTION_ENTER_AGE = "Please also give us your age (number only).\n";
	public static final String ERROR_REENTER_AGE = "Invalid input! Please input your age (number only).";
	public static final String INSTRUCTION_BOOKING = "Thank you for your interest, here is a list of tours:\n"
			+ "Attention: You can terminate the booking procedure by texting \"Q\" at any time!\n\n";
	public static final String INSTRTUCTION_ENTER_TOUR_ID = "\nPlease enter one of the tour IDs (number only).\n";
	public static final String ERROR_REENTER_TOUR_ID = "Invalid tour ID! Please reinput tour ID.";
	public static final String INFORMATION_NO_TOUR_OFFERING = "Sorry, currently we do not provide any offerings for this tour!\n"
			+ "Please choose another tour id!";
	public static final String INSTRTUCTION_ENTER_TOUR_OFFERING_ID = "Please enter one of the tour offering IDs (number only).";
	public static final String ERROR_REENTER_TOUR_OFFERING_ID = "Invalid tour offering ID! Please reinput tour offering ID.";
	public static final String INSTRTUCTION_ENTER_ADULT_NUMBER = "Please input the number of adults for this tour offering.";
	public static final String ERROR_REENTER_ADULT_NUMBER = "Invalid number! Please reinput the number of adults.";
	public static final String INSTRTUCTION_ENTER_CHILDREN_NUMBER = "Please input the number of childrens (age between 4 and 11) for this tour offering.";
	public static final String ERROR_REENTER_CHILDREN_NUMBER = "Invalid number! Please reinput the number of children.";
	public static final String INSTRTUCTION_ENTER_TODDLER_NUMBER = "Please input the number of toddlers (age not larger than 3) for this tour offering.";
	public static final String ERROR_REENTER_TODDLER_NUMBER = "Invalid number! Please reinput the number of toddlers.";
	public static final String INSTRTUCTION_ENTER_SPECIAL_REQUEST = "Please leave your special request.";
	public static final String CONFIRM_REGISTRATION = "Great! Basic information registered!\n";
	public static final String INSTRUCTION_PAYMENT = "Thanks for your interest!\n"
			+ "Please pay your tour fee by ATM to 123-345-432-211 of ABC Bank or by cash in our store.\n"
			+ "You shall send you pay-in slip to us by email or LINE. \n"
			+ "You are welcome to ask other questions!";
	public static final String QUESTION_REVIEW_OR_BOOKING = "Do you want to review your previous booking or do you want to start a new booking?";
	public static final String QUESTION_CONFIRM_OR_NOT = "Do you want to confirm your booking";
	public static final String FAQ_NOT_FOUND = "Sorry! We don't have relevant information.";
	public static final String BOOKING_CANCELLED = "Booking cancelled!";
	

}
