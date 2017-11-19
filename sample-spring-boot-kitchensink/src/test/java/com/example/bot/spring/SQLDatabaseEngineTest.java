/*package com.example.bot.spring;


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
import org.junit.Ignore;
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
@SpringBootTest(classes = { SQLDatabaseEngineTest.class,  SQLDatabaseEngine.class })
// @Ignore
public class SQLDatabaseEngineTest {
	@Autowired
	private SQLDatabaseEngine databaseEngine;

	private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
	private static final java.util.Date now = calendar.getTime();
	private static final java.sql.Timestamp time = new java.sql.Timestamp(now.getTime());

	private static final int test_tour_id = 2;
	private static final int test_tour_offering_id_no_discount = 17;
	private static final String test_user_id = "test_id";
	private static final String test_name = "test_name";
	private static final String test_phoneno = "00001111";
	private static final String test_age = "20";
	private static final int test_state = 1;

	private static boolean init = false;
	private static boolean thrown = false;
	private static boolean result = true;

	@Before
	public void setUp() {
        try {
            init = true;
            databaseEngine.createUser(test_user_id, time, test_state);
        } catch (Exception e) {
            log.info("Test User Exist!");
        }
		thrown = false;
		result = true;
	}

	@After
	public void check() {
		assertFalse(thrown);
		log.info("No Exception");
		assertTrue(result);
        log.info("Update Succeed");
        try {
			Boolean deleted= databaseEngine.deleteUser(test_user_id);
			if (!deleted) {
				log.info("Delete fail!");
			}
        }
        catch (Exception e) {
            log.info(e.toString());
        }
	}
	
	@Test
	public void testSetInformation() {
		// for testing this class TourOffering tourOfferingFound
		try {
			databaseEngine.deleteUser(test_user_id);
			result = databaseEngine.createUser(test_user_id, time, test_state);
			result = databaseEngine.setUserAge(test_user_id, test_age);
			result = databaseEngine.setUserPhoneNum(test_user_id, test_phoneno)&&result;
			result = databaseEngine.setUserName(test_user_id, test_name)&&result;
			result = databaseEngine.setUserState(test_user_id, test_state)&&result;
			result = databaseEngine.setUserTime(test_user_id, time)&&result;
			result = databaseEngine.tourFound(test_tour_id )&&result;
			databaseEngine.getUserInformation(test_user_id);
			databaseEngine.getTours(test_user_id);
			
		} catch (Exception e) {
			thrown = true;
		}
	}
	@Test
	public void testTourOfferingFound() {
		// for testing this class TourOffering tourOfferingFound
		try {
			result = databaseEngine.tourOfferingFound(test_tour_id, test_tour_offering_id);
		} catch (Exception e) {
			thrown = true;
		}
	}

	@Test
	public void testDisplayTourOffering() {
		//for testing this class TourOffering displayTourOffering
		try {
			if (databaseEngine.displayTourOffering(test_tour_id).isEmpty()) {
				result = false;
			}
		} catch (Exception e) {
			thrown = true;
		}
	}
	@Test
	public void testDisplaytBookingInformation() {
		//for testing this class TourOffering displayTourOffering
		try {
			if (databaseEngine.displaytBookingInformation(test_user_id ) == null) {
				result = false;
			}
		} catch (Exception e) {
			thrown = true;
		}
	}


	@Test
	public void testBuffer() {
		//for testing this class Booking Buffer
		try {
			result = databaseEngine.setBufferTourID(test_user_id, test_tour_id);
		} catch (Exception e) {
			thrown = true;
		}

		if (thrown || !result) {
				return;
		}

		try {
			result = (databaseEngine.getBufferTourID(test_user_id) != -1);
		} catch (Exception e) {
			thrown = true;
		}

		if (thrown || !result) {
				return;
		}

		try {
			result = databaseEngine.deleteBufferBookingEntry(test_user_id);
		} catch (Exception e) {
			thrown = true;
		}
	}

	@Test
	public void testSetBookingTourOfferingID() {
		//for testing this class Booking setBookingTourOfferingID
		try {
			result =databaseEngine.setBookingTourOfferingID(test_user_id, test_tour_offering_id);
		} catch (Exception e) {
			thrown = true;
		}
	}
	
	@Test
	public void testAddToUnknowndatabase() {
		//for testing AddToUnknowndatabase()
		try {
			result =databaseEngine.addToUnknownDatatabse("Xuxiaofeng");
			
		} catch (Exception e) {
			thrown = true;
		}
	}
	
}
*/