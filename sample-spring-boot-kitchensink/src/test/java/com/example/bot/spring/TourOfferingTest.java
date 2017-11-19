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
import org.junit.Ignore;
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

//import com.example.bot.spring.DatabaseEngine;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

@Slf4j
@RunWith(SpringRunner.class)
// @SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { TourOfferingTest.class })
//@Ignore
public class TourOfferingTest {
	@Autowired

	private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
	private static final java.util.Date now = calendar.getTime();
	private static final java.sql.Timestamp time = new java.sql.Timestamp(now.getTime());

	private static final int test_offeringID = 10;
	private static final String test_date = "test_name";
	private static final String test_guideName = "hhh";
	private static final String test_guideAccount = "abc";
	private static final String test_hotel = "def";
	private static final int test_maxCapacity = 20;
	private static final int test_minCapacity = 10;
	private static final int test_state = 2;
	private static final int test_price = 10;
	private static final int test_duration = 2;
	private static final int test_quota = 15;
	
	private static boolean init = false;
	private static boolean thrown = false;
	private static String query_result = null;
	private static boolean update_result = true;
	
				
	private static TourOffering newtouroffering = new TourOffering(test_offeringID, test_date, test_hotel, test_maxCapacity,
			test_price,test_duration,test_quota,test_guideName,test_guideAccount,test_minCapacity,test_state);	

	@Test 
	public void testConstructor() throws Exception {
		try {
			TourOffering touroffering1 = new TourOffering(test_offeringID, test_date, test_hotel, test_maxCapacity,
					test_price,test_duration,test_quota,test_guideName,test_guideAccount,test_minCapacity,test_state);	
			TourOffering touroffering2 = new TourOffering(test_offeringID, test_date, test_hotel, test_maxCapacity,
					test_price,test_duration,test_quota);	
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	@Test
	public void testSetQuota() throws Exception {
		try {
			newtouroffering.setQuota(test_quota);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	@Test
	public void testSetDuration() throws Exception {
		try {
			newtouroffering.setDuration(test_duration);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}

	@Test
	public void testSetPrice() throws Exception {
		try {
			newtouroffering.setPrice(test_price);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	@Test
	public void testSetState() throws Exception {
		try {
			newtouroffering.setState(test_state);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}

	@Test
	public void testSetMinCapacity() throws Exception {
		try {
			newtouroffering.setMinCapacity(test_minCapacity);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	@Test
	public void testSetMaxCapacity() throws Exception {
		try {
			newtouroffering.setMaxCapacity(test_maxCapacity);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	@Test
	public void testSetHotel() throws Exception {
		try {
			newtouroffering.setHotel(test_hotel);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	@Test
	public void testSetGuideAccount() throws Exception {
		try {
			newtouroffering.setGuideAccount(test_guideAccount);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	@Test
	public void testSetGuideName() throws Exception {
		try {
			newtouroffering.setGuideName(test_guideName);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	@Test
	public void testSetDate() throws Exception {
		try {
			newtouroffering.setDate(test_date);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	@Test
	public void testSetOfferingID() throws Exception {
		try {
			newtouroffering.setOfferingID(test_offeringID);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}

	
	
	
	@Test
	public void testGetQuota() throws Exception {
		//for testing User class setUserAge function
		try {
			int id =newtouroffering.getQuota();
			assertTrue(id==test_quota);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	@Test
	public void testGetDuration() throws Exception {
		//for testing User class setUserAge function
		try {
			int duration =newtouroffering.getDuration();
			assertTrue(duration==test_duration);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	@Test
	public void testGetPrice() throws Exception {
		//for testing User class setUserAge function
		try {
			int price =newtouroffering.getPrice();
			assertTrue(price==test_price);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	@Test
	public void testGetState() throws Exception {
		//for testing User class setUserAge function
		try {
			int state =newtouroffering.getState();
			assertTrue(state==test_state);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	@Test
	public void testGetMinCapacity() throws Exception {
		//for testing User class setUserAge function
		try {
			int min =newtouroffering.getMinCapacity();
			assertTrue(min==test_minCapacity);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	@Test
	public void testGetMaxCapacity() throws Exception {
		//for testing User class setUserAge function
		try {
			int max =newtouroffering.getMaxCapacity();
			assertTrue(max==test_maxCapacity );
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	@Test
	public void testGetOfferingID() throws Exception {
		//for testing User class setUserAge function
		try {
			int id =newtouroffering.getOfferingID();
			assertTrue(id==test_offeringID);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	@Test
	public void testGetHotel() throws Exception {
		//for testing User class setUserAge function
		try {
			String Hotel =newtouroffering.getHotel();
			assertTrue(Hotel.equals(test_hotel));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	@Test
	public void testGetGuideAccount() throws Exception {
		//for testing User class setUserAge function
		try {
			String GuideAccount =newtouroffering.getGuideAccount();
			assertTrue(GuideAccount.equals(test_guideAccount));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	@Test
	public void testGetGuideName() throws Exception {
		//for testing User class setUserAge function
		try {
			String GuideName =newtouroffering.getGuideName();
			assertTrue(GuideName.equals(test_guideName));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	@Test
	public void testGetDate() throws Exception {
		//for testing User class setUserAge function
		try {
			String Date =newtouroffering.getDate();
			assertTrue(Date.equals(test_date));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

}
