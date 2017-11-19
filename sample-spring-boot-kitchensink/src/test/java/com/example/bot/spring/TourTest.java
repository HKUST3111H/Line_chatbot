package com.example.bot.spring;


import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.example.bot.spring.DatabaseEngine;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

@Slf4j
@RunWith(SpringRunner.class)
// @SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { TourTest.class })
public class TourTest {
	@Autowired

	private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
	private static final java.util.Date now = calendar.getTime();
	private static final java.sql.Timestamp time = new java.sql.Timestamp(now.getTime());

	private static final int test_tour_id = 10;
	private static final String test_name = "test_name";
	private static final String test_description = "hhh";
	private static final int test_duration = 2;

	private static boolean init = false;
	private static boolean thrown = false;
	private static String query_result = null;
	private static boolean update_result = true;
	
				
	private static Tour newtour = new Tour(test_tour_id, test_name, test_description, test_duration);	

	@Test 
	public void testConstructor() throws Exception {
		try {
			Tour consTour = new Tour(test_tour_id, test_name, test_description, test_duration);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	
	
	
	@Test
	public void testSetTourID() throws Exception {
		try {
			newtour.setTourID(test_tour_id);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	@Test
	public void testSetTourName() throws Exception {
		try {
			newtour.setTourName(test_name);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}

	@Test
	public void testSetDescription() throws Exception {
		try {
			newtour.setDescription(test_description);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	@Test
	public void testSetDuration() throws Exception {
		try {
			newtour.setDuration(test_duration);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}

	
	@Test
	public void testGetTourID() throws Exception {
		//for testing User class setUserAge function
		try {
			int id =newtour.getTourID();
			assertTrue(id==test_tour_id);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	@Test
	public void testGetTourName() throws Exception {
		//for testing User class setUserAge function
		try {
			String TourName =newtour.getTourName();
			assertTrue(TourName.equals(test_name));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	@Test
	public void testGetDescription() throws Exception {
		//for testing User class setUserAge function
		try {
			String description =newtour.getDescription();
			assertTrue(description.equals(test_description));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	
	@Test
	public void testGetDuration() throws Exception {
		//for testing User class setUserAge function
		try {
			int duration =newtour.getDuration();
			assertTrue(duration==test_duration);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	
	
	
	
	

}
