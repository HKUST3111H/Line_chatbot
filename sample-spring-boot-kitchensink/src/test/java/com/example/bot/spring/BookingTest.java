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
@SpringBootTest(classes = { UserTest.class,  SQLDatabaseEngine.class })
public class BookingTest {
	@Autowired
	private SQLDatabaseEngine databaseEngine;

	private static final String test_user_id = "test_id";
	private static final int test_tour_id = 12;
	private static final int test_tour_offering_id = 1;
	private static boolean thrown = false;
	private static boolean result = false;

	@Before
	public void setUp() {
		thrown = false;
		result = true;
	}

	@After
	public void check() {
		assertFalse(thrown);
		log.info("No Exception");
		assertTrue(result);
		log.info("Update Succeed");
	}

	@Test
	public void testBuffer() throws Exception {
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
	public void testSetBookingTourOfferingID() throws Exception {
		//for testing this class Booking setBookingTourOfferingID
		try {
			result =databaseEngine.setBookingTourOfferingID(test_user_id, test_tour_offering_id);
		} catch (Exception e) {
			thrown = true;
		}
	}

}
