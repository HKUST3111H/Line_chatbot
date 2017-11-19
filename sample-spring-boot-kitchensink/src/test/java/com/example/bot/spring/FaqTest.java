package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Ignore;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Matchers.any;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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

//@RunWith(SpringRunner.class)
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = { FaqTest.class, FaqDatabase.class })
//@Ignore
public class FaqTest {

	// @Autowired
	// private FaqDatabase faqEngine;

	@Mock
	private PreparedStatement stmt;
	
	@InjectMocks
	private FaqDatabase faqEngine;

	@Test
	public void testReplyImageFind() throws Exception {
		boolean thrown = false;
		try {
			String result = this.faqEngine.replyImage("(see the picture gather.jpg)");
			System.out.println("\n\n\n\n");
			System.out.println(result);
			System.out.println("\n\n\n\n");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
	}

	@Test
	public void testReplyImageNotFind() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.faqEngine.replyImage("not find");
			System.out.println("\n\n\n\n");
			System.out.println(result);
			System.out.println("\n\n\n\n");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
		assertThat(result).isEqualTo(null);
	}

	@Test
	public void testLoadQuestionSQL() throws Exception {
		boolean thrown = false;
		try {
			List<faqEntry> result = this.faqEngine.loadQuestionSQL();
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
	}

	@Test
	public void testUpdateHit() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			this.faqEngine.updateHit(42, 0);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
	}

	@Test
	public void testSearchFoundHotTour() throws Exception {
		boolean thrown = false;
		try {
			String result = this.faqEngine.search("hot tour, popular attractions", "U52a29b672ee486b66b7fb4c45a888de3");
			System.out.println("\n\n\n\n");
			System.out.println(result);
			System.out.println("\n\n\n\n");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
	}

	@Test
	public void testSearchFoundMoreTours() throws Exception {
		boolean thrown = false;
		try {
			String result = this.faqEngine.search("more tours", "U52a29b672ee486b66b7fb4c45a888de3");
			System.out.println("\n\n\n\n");
			System.out.println(result);
			System.out.println("\n\n\n\n");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
	}

	@Test
	public void testSearchToursWithHotSpring() throws Exception {
		boolean thrown = false;
		try {
			String result = this.faqEngine.search("tours with hot spring", "U52a29b672ee486b66b7fb4c45a888de3");
			System.out.println("\n\n\n\n");
			System.out.println(result);
			System.out.println("\n\n\n\n");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
	}
	

	//@Test
	//public void test_dynamic_top_five_tours_catch() throws Exception {
	//	doThrow(new Exception()).when(stmt).setString(1, "U52a29b672ee486b66b7fb4c45a888de3");
		//when(this.faqEngine.dynamic_top_five_tours("U52a29b672ee486b66b7fb4c45a888de3")).thenThrow(new Exception());


	@Test
	public void testSearchToursWithMountain() throws Exception {
		boolean thrown = false;
		try {
			String result = this.faqEngine.search("tours with mountain", "U52a29b672ee486b66b7fb4c45a888de3");
			System.out.println("\n\n\n\n");
			System.out.println(result);
			System.out.println("\n\n\n\n");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
	}

	@Test
	public void testParse() throws Exception {
		boolean thrown = false;
		String description = "123*456*789";
		String result = "";
		try {
			result = this.faqEngine.parse(description);
			System.out.println("\n\n\n\n");
			System.out.println(result);
			System.out.println("\n\n\n\n");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
		assertThat(result.equals("123\n*456\n*789")).isEqualTo(true);
	}

	@Test
	public void testSearchFound() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.faqEngine.search("How to apply?", "U52a29b672ee486b66b7fb4c45a888de3");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
		assertThat(result).contains(
				"Customers shall approach the company by phone or visit our store (in Clearwater bay) with the choosen tour code and departure date. "
						+ "If it is not full, customers will be advised by the staff to pay the tour fee. "
						+ "Tour fee is non refundable. Customer can pay their fee by ATM to 123-345-432-211 of ABC Bank or by cash in our store. "
						+ "Customer shall send their pay-in slip to us by email or LINE.");

	}

	@Test
	public void testSearchNotFound() throws Exception {
		boolean thrown = false;
		String result = null;
		try {
			result = this.faqEngine.search("balabalabala", "U52a29b672ee486b66b7fb4c45a888de3");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(true);
		assertThat(result).isEqualTo(null);

	}

}
