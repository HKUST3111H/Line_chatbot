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
public class UserTest {
	
	@Autowired
	private SQLDatabaseEngine databaseEngine;

	private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
	private static final java.util.Date now = calendar.getTime();
	private static final java.sql.Timestamp time = new java.sql.Timestamp(now.getTime());

	private static final String test_user_id = "test_user_id";
	private static final String test_name = "test_name";
	private static final String test_phoneno = "00001111";
	private static final String test_age = "20";
	private static final int test_state = 1;

	private static boolean init = false;
	private static boolean thrown = false;
	private static String query_result = null;
	private static boolean update_result = true;
	
	private static User newuser = new User(test_user_id, test_name, test_phoneno, test_age,test_state,time );	
				

	// public UserTest() {
	// 	try {
	// 		databaseEngine.createUser(test_user_id, time, test_state);
	// 	} catch (Exception e) {
	// 		log.info("Test User Exist!");
	// 	}
	// }
	@Before
	public void setUp() {
		if (!init) {
			try {
				init = true;
				databaseEngine.createUser(test_user_id, time, test_state);
			} catch (Exception e) {
				log.info("Test User Exist!");
			}
		}
		thrown = false;
		update_result = true;
	}

	@After
	public void check() {
		assertFalse(thrown);
		log.info("No Exception");
		assertTrue(update_result);
		log.info("Update Succeed");
	}

	@Test
	public void testCreateUser() throws Exception {
		//for testing User class createUser function
		try {
			log.info(test_user_id);
			databaseEngine.deleteUser(test_user_id);
			update_result = databaseEngine.createUser(test_user_id, time, test_state);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

	@Test
	public void testSetUserTime() throws Exception {
		//for testing User class setUserTime function
		try {
			update_result = databaseEngine.setUserTime(test_user_id, time);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

	@Test
	public void testSetUserState() throws Exception {
		//for testing User class setUserState function
		try {
			update_result = databaseEngine.setUserState(test_user_id, test_state);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}


	@Test
	public void testSetUserName() throws Exception {
		//for testing User class setUserName function
		try {
			update_result = databaseEngine.setUserName(test_user_id, test_name);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

	@Test
	public void testSetUserPhoneNum() throws Exception {
		//for testing User class setUserPhoneNum function
		try {
			update_result = databaseEngine.setUserPhoneNum(test_user_id, test_phoneno);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

	@Test
	public void testSetUSerAge() throws Exception {
		//for testing User class setUserAge function
		try {
			update_result = databaseEngine.setUserAge(test_user_id, test_age);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
//=======================for user calss====================
	@Test 
	public void testConstructor() throws Exception {
		try {
			User consUser = new User(test_user_id, test_name, test_phoneno, test_age,test_state,time );	
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	
	@Test
	public void testSetTime() throws Exception {
		try {
			newuser.setTime(time);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	
	@Test
	public void testSetName() throws Exception {
		try {
			newuser.setName(test_name);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	@Test
	public void testSetState() throws Exception {
		try {
			newuser.setState(test_state);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}

	@Test
	public void testSetAge() throws Exception {
		try {
			newuser.setAge(test_age);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}

	

	@Test
	public void testSetID() throws Exception {
		try {
			newuser.setID(test_user_id);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	@Test
	public void testSetPhoneNumber() throws Exception {
		try {
			newuser.setPhoneNumber(test_phoneno);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	@Test
	public void testAddTripHistory() throws Exception {
		try {
			newuser.addTripHistory("Shenzhen City");
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	@Test
	public void testSetUser() throws Exception {
		try {
			
			newuser.setUser(test_user_id,time,test_state);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	
	}
	
	
	
	@Test
	public void testGetUserName() throws Exception {
		//for testing User class setUserAge function
		try {
			String name =newuser.getUserName();
			assertTrue(name.equals(test_name));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	@Test
	public void testGetPhoneNumber() throws Exception {
		//for testing User class setUserAge function
		try {
			String phoneNumber =newuser.getPhoneNumber();
			assertTrue(phoneNumber.equals(test_phoneno));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	@Test
	public void testGetUserID() throws Exception {
		//for testing User class setUserAge function
		try {
			String ID =newuser.getUserID();
			assertTrue(ID.equals(test_user_id));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	
	@Test
	public void testGetAge() throws Exception {
		//for testing User class setUserAge function
		try {
			String age =newuser.getAge();
			assertTrue(age.equals(test_age));
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	
	@Test
	public void testGetState() throws Exception {
		//for testing User class setUserAge function
		try {
			int state =newuser.getState();
			assertTrue(state==test_state);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	@Test
	public void testGetTime() throws Exception {
		//for testing User class setUserAge function
		try {
			java.sql.Timestamp thisTime =newuser.getTime();
			assertTrue(thisTime==time);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	
	@Test
	public void testOutputTripHistory() throws Exception {
		//for testing User class setUserAge function
		try {
			newuser.outputTripHistory();
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}
	
	
	
	
	
	

}
