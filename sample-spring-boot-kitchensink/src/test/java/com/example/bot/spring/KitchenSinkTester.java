package com.example.bot.spring;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { KitchenSinkTester.class, SQLDatabaseEngine.class, FaqDatabase.class })
public class KitchenSinkTester {
	@Autowired
	private SQLDatabaseEngine databaseEngine;
	
	@Autowired
	private FaqDatabase faqEngine;
	
	
	@Test
	public void testFaqNotFound() throws Exception {
		boolean thrown = false;
		try {
			this.faqEngine.search("i wanna eat an apple");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(true);
	}
	
	@Test
	public void testFaqFound() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result=this.faqEngine.search("How to apply?");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
		assertThat(result).contains("Customers shall approach the company by phone or visit our store (in Clearwater bay) with the choosen tour code and departure date. If it is not full, customers will be advised by the staff to pay the tour fee. Tour fee is non refundable. Customer can pay their fee by ATM to 123-345-432-211 of ABC Bank or by cash in our store. Customer shall send their pay-in slip to us by email or LINE.");

	}
	@Test
	public void testFaqFound2() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result=this.faqEngine.search("serve vegeterian");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
		assertThat(result).contains("No");
	}
	
	@Test
	public void testNotFound() throws Exception {
		boolean thrown = false;
		try {
			this.databaseEngine.search("no");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown);
	}
	
	@Test
	public void testFound() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("abc");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isEqualTo("def");
	}
	@Test
	public void testFound_1() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("hahaabc");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isEqualTo("def");
	}
	@Test
	public void testFound_2() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.search("I am fine");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result.equals("Great!"));
	}
	@Test
	public void testUser() throws Exception {
		//for testing this class
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp time = new java.sql.Timestamp(now.getTime());
		User userA = new User("Xiaofeng","Xiaofeng","Xiaofeng","Xiaofeng",0,time);
		userA.setAge("45");
		userA.setPhoneNumber("888888");
		System.out.println(userA.getAge());
		System.out.println(userA.getAge());
		userA.addTripHistory("hkust");
		userA.addTripHistory("hongkong");
		userA.addTripHistory("tomorrowland");
		userA.outputTripHistory();
		
	}
	
	@Test
	public void testTour() throws Exception {
		//for testing this class
		Tour tour = new Tour("ID","Name","Description",2);
		TourOffering offering = new TourOffering("offeringID", "date", "guideName", "guideAccount",
				"hotel", 40, 5);
		tour.addTourOffering(offering);
		tour.removeTourOffering(offering);
		System.out.println(tour.getTourID());
		System.out.println(offering.getGuideName());
		assertThat(tour.getTourID()).isEqualTo("ID");
		assertThat(offering.getOfferingID()).isEqualTo("offeringID");
		
	}
	
	@Test
	public void testSQL() throws Exception {
		//for testing this class
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp time = new java.sql.Timestamp(now.getTime());
		this.databaseEngine.setUserTime("test", time);
		
	}
	
	@Test
	public void testSQL_2() throws Exception {
		//for testing this class
		this.databaseEngine.setUserState("test", 2);
	}
	
	@Test
	public void testSQL_3() throws Exception {
		//for testing this class
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp time = new java.sql.Timestamp(now.getTime());
		this.databaseEngine.createUser("test4",time, 0);
	}

	@Test
	public void testSQL_4() throws Exception {
		//for testing this class
		this.databaseEngine.setUserName("test", "xxf");
	}
	@Test
	public void testSQL_5() throws Exception {
		//for testing this class
		this.databaseEngine.setUserPhoneNum("test", "10086");
	}
	
	@Test
	public void testSQL_6() throws Exception {
		//for testing this class
		this.databaseEngine.setUserAge("test", "100");
	}
	
	@Test
	public void testSQL_7() throws Exception {
		//for testing this class
		boolean thrown = false;
		boolean result = false;
		try {
			result = this.databaseEngine.tourOfferingFound(12,1);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isEqualTo(true);
	}
	
	@Test
	public void testSQL_10() throws Exception {
		//for testing this class
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.displayTourOffering(12);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isNotEqualTo("null");
	}
	
	
	@Test
	public void testSQL_12() throws Exception {
		//for testing this class
		boolean thrown = false;
		int result = -1;
		boolean result2 = false;
		try {
			result2 = this.databaseEngine.setBufferTourID("test", 12);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result2).isEqualTo(true);
		
		try {
			result = this.databaseEngine.getBufferTourID("test");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isNotEqualTo(-1);
		
		try {
			result2 = this.databaseEngine.deleteBufferBookingEntry("test");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result2).isEqualTo(true);
	}
	@Test
	public void testSQL_13() throws Exception {
		//for testing this class
		boolean thrown = false;
		boolean result = false;
		try {
			result = this.databaseEngine.setBookingTourOfferingID("test4",1);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isEqualTo(true);
	}
}
