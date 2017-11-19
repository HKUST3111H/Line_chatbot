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

//import com.example.bot.spring.DatabaseEngine;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

@Slf4j
@RunWith(SpringRunner.class)
// @SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { SQLDatabaseEngineTest.class,  SQLDatabaseEngine.class })
//@Ignore
public class SQLDatabaseEngineTest {
	@Autowired
	private SQLDatabaseEngine databaseEngine;

	private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
	private static final java.util.Date now = calendar.getTime();
	private static final java.sql.Timestamp time = new java.sql.Timestamp(now.getTime());

	private static final int test_tour_id = 8;
	private static final int test_tour_offering_id_no_discount = 18;
	private static final String test_user_id = "test_id";
	private static final String test_user_id_1 = "test_id1";
	private static final String test_user_id_2 = "test_id2";
	private static final String test_user_id_3 = "test_id3";
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
            databaseEngine.createUser(test_user_id_2, time, test_state);
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
			Boolean deleted= databaseEngine.deleteUser(test_user_id_2);
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
				result = databaseEngine.createUser(test_user_id_1, time, test_state);
				log.info("11");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				result = databaseEngine.setUserAge(test_user_id_1, test_age);
				log.info("12");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				result = databaseEngine.setUserPhoneNum(test_user_id_1, test_phoneno);
				log.info("13");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				result = databaseEngine.setUserName(test_user_id_1, test_name);
				log.info("14");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				result = databaseEngine.setUserState(test_user_id_1, test_state);
				log.info("15");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				result = databaseEngine.setUserTime(test_user_id_1, time);
				log.info("16");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				result = databaseEngine.tourFound(test_tour_id );
				log.info("17");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				databaseEngine.getUserInformation(test_user_id_1);
				log.info("18");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				if (databaseEngine.getTours().isEmpty()) {
					result = false;
				}
				log.info("19");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				result =databaseEngine.addToUnknownDatatabse("Xuxiaofeng");
				log.info("20");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				if (databaseEngine.displayTourOffering(test_tour_id).isEmpty()) {
					result = false;
				}
				log.info("21");
			} catch (Exception e) {
				thrown = true;
			}

			if (thrown || !result) {
					return;
			}
			try {
				result = databaseEngine.tourOfferingFound(test_tour_id,test_tour_offering_id_no_discount);
				log.info("22");
			} catch (Exception e) {
				thrown = true;
			}
			if (thrown || !result) {
				return;
			}
			try {
				databaseEngine.deleteUser(test_user_id_1);
				log.info("23");
			} catch (Exception e) {
				thrown = true;
			}
	}
	
	@Test
	public void testSetBooking() {
		// for testing this class TourOffering tourOfferingFound
		try {
			databaseEngine.createUser(test_user_id, time, test_state);
			result = databaseEngine.setBufferTourID(test_user_id, test_tour_id);
			log.info("31");
		} catch (Exception e) {
			thrown = true;
		}

		if (thrown || !result) {
				return;
		}

		try {
			result = (databaseEngine.getBufferTourID(test_user_id) != -1);
			log.info("32");
		} catch (Exception e) {
			thrown = true;
		}

		if (thrown || !result) {
				return;
		}

		try {
			result = databaseEngine.deleteBufferBookingEntry(test_user_id);
			log.info("33");
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			result = databaseEngine.setBookingTourOfferingID(test_user_id, test_tour_offering_id_no_discount);
			log.info("34");
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			result = databaseEngine.deleteBookingEntry(test_user_id);
			log.info("35");
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			databaseEngine.setBookingTourOfferingID(test_user_id, test_tour_offering_id_no_discount);
			result = databaseEngine.setBookingAdultNumber(test_user_id, 1);
			log.info("36");
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			result = databaseEngine.setBookingChildrenNumber(test_user_id,1);
			log.info("37");
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			result = databaseEngine.setBookingToddlerNumber(test_user_id,1);
			log.info("38");
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			int quota=databaseEngine.checkQuota(test_user_id);
			if(quota<-1) {
				result = false;
			}
			log.info("39");
			log.info(Integer.toString(quota));
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			result = databaseEngine.setBookingSpecialRequest(test_user_id,"No");
			log.info("40");
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			result = databaseEngine.setBookingConfirmation(test_user_id);
			log.info("41");
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			if(databaseEngine.displaytBookingInformation(test_user_id).isEmpty()) {
				result = false;
			}
			log.info("42");
		} catch (Exception e) {
			thrown = true;
		}
		if (thrown || !result) {
			return;
		}
		try {
			if(databaseEngine.reviewBookingInformation(test_user_id )==null) {
				result = false;
			}
			log.info("43");
			databaseEngine.deleteUser(test_user_id);
		} catch (Exception e) {
			thrown = true;
		}
	}
	
	@Test
	public void testBookingInformation() {
		//for testing this class TourOffering displayTourOffering
		try {
			if (databaseEngine.displaytBookingInformation(test_user_id_2 ) == null
					&&databaseEngine.reviewBookingInformation(test_user_id_2 ) == null) {
				result = false;
			}
			databaseEngine.getUserInformation(test_user_id_1);
			databaseEngine.tourOfferingFound(test_tour_id,0);
			databaseEngine.displaytBookingInformation(test_user_id_1);
		} catch (Exception e) {
			thrown = true;
		}
	}


	@Test
	public void testCalculateDiscount() {
		//for testing this class Booking setBookingTourOfferingID
		try {
			databaseEngine.calculateDiscount(0, 0, 0, 0,0,
					0, 0, 0);
			databaseEngine.calculateDiscount(5, 6, 0, 0,0,
					0, 0, 0);
			databaseEngine.calculateDiscount(5, 1, 0, 0,0,
					0, 1, 0);
			databaseEngine.calculateDiscount(5, 1, 0, 0,0,
					0, 6, 0);
		} catch (Exception e) {
			thrown = true;
		}
	}
	
}
