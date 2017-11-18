package com.example.bot.spring;

import static java.util.Collections.singletonList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

@RunWith(MockitoJUnitRunner.class)
public class LineMessageControllerTest {

	@Mock
	private LineMessagingClient lineMessagingClient;
	@Mock
	private SQLDatabaseEngine database;
	@InjectMocks
	private LineMessageController underTest;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test_FAQ_NO_USER_INFORMATION_handler() throws Exception {

		String testMsg = "blablablabla";
		String userId = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.FAQ_NOT_FOUND;

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));

		underTest.FAQ_NO_USER_INFORMATION_handler(replyToken, testMsg, userId, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}

	@Test
	public void test_FAQ_NO_USER_INFORMATION_handler_booking() throws Exception {

		String testMsg = "booking";
		String userId = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.INSTRUCTION_FILL_INFORMATION + Constant.INSTRUCTION_ENTER_NAME;

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));

		when(database.setUserState(userId, Constant.FILL_NAME)).thenReturn(true);

		underTest.FAQ_NO_USER_INFORMATION_handler(replyToken, testMsg, userId, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}

	@Test
	// test faq succesful search for text
	public void test_faqsearch() throws Exception {

		String testMsg = "no.";
		String userId = "userId";
		String replyToken = "replyToken";
		String expectReply = "Got it.";
		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));

		when(database.setUserState(userId, Constant.FILL_NAME)).thenReturn(true);

		underTest.faqsearch(replyToken, testMsg, "", userId);

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}

	// test faq succesful search for image

	// code here
	/*
	@Test
	public void test_FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION_handler() throws Exception {

		String testMsg = "bala";
		String userId = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.INSTRUCTION_FILL_INFORMATION + Constant.INSTRUCTION_ENTER_NAME;

	}

	@Test

	public void test_FAQ_AFTER_CONFIRMATION_handler() throws Exception {

		String testMsg = "bala";
		String userId = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.INSTRUCTION_FILL_INFORMATION + Constant.INSTRUCTION_ENTER_NAME;

	}
	*/

	@Test

	public void test_FILL_NAME_handler() throws Exception {

		String testMsg = "blablablabla";
		String userId = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.INSTRUCTION_ENTER_PHONE_NUM;

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		when(database.setUserState(userId, Constant.FILL_PHONE_NUM)).thenReturn(true);
		when(database.setUserName(userId, testMsg)).thenReturn(true);

		underTest.FILL_NAME_handler(replyToken, testMsg, userId, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}

	@Test

	public void test_FILL_PHONE_NUM_handler() throws Exception {

		String testMsg = "blablablabla";
		String userId = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.INSTRUCTION_ENTER_AGE;

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		when(database.setUserState(userId, Constant.FILL_AGE)).thenReturn(true);
		when(database.setUserPhoneNum(userId, testMsg)).thenReturn(true);

		underTest.FILL_PHONE_NUM_handler(replyToken, testMsg, userId, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}

	@Test
	// for non numeric number e.g balabala
	public void test_FILL_AGE_handler_invalid() throws Exception {

		String testMsg = "blablablabla";
		String userId = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.ERROR_REENTER_AGE;

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));

		underTest.FILL_AGE_handler(replyToken, testMsg, userId, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}
/*
	@Test
    //when age = "23", a valid input, incomplete 
    public void test_FILL_AGE_handler() throws Exception {

        String testMsg = "23";
        String userId = "userId";
        String replyToken = "replyToken";
        String expectReply = Constant.CONFIRM_REGISTRATION;
        
        //message list when needed
        List<Message> messages = new ArrayList<Message>();
        messages.add(new TextMessage(expectReply));
        
        
        // mock line bot api client response
        when(lineMessagingClient.replyMessage(new ReplyMessage(
                replyToken, messages)
        ))).thenReturn(CompletableFuture.completedFuture(
                new BotApiResponse("ok", Collections.emptyList())
        ));
        when(database.setUserState(userId, Constant.BOOKING_TOUR_ID)).thenReturn(true);
        when(database.setUserAge(userId, testMsg)).thenReturn(true);

        
        underTest.FILL_AGE_handler(replyToken, testMsg, userId, "");

        // confirm replyMessage is called with following parameter
        verify(lineMessagingClient).replyMessage(new ReplyMessage(
                replyToken, messages)
        ));
    }
    */

	@Test
	// for non numeric number e.g balabala
	public void test_BOOKING_TOUR_ID_handler() throws Exception {

	}

	@Test
	// for Quit case
	public void test_BOOKING_OFFERING_ID_handler_Quit() throws Exception {

		String text = "Q";
		String userID = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.CANCEL;

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		when(database.reviewBookingInformation(userID)).thenReturn("null");
		when(database.reviewBookingInformation(userID)).thenReturn("null");
		when(database.setUserState(userID, Constant.FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION)).thenReturn(true);
		when(database.setUserState(userID, Constant.FAQ_AFTER_CONFIRMATION)).thenReturn(true);
		when(database.deleteBookingEntry(userID)).thenReturn(true);
		when(database.deleteBufferBookingEntry(userID)).thenReturn(true);
		underTest.BOOKING_OFFERING_ID_handler(replyToken,text, userID, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}

	@Test
    //for successful offering id
    public void test_BOOKING_OFFERING_ID_handler_success() throws Exception {

        String text = "12";
        String userID = "userId";
        String replyToken = "replyToken";
        String expectReply = Constant.INSTRTUCTION_ENTER_ADULT_NUMBER;

        // mock line bot api client response
        when(lineMessagingClient.replyMessage(new ReplyMessage(
                replyToken, singletonList(new TextMessage(expectReply))
        ))).thenReturn(CompletableFuture.completedFuture(
                new BotApiResponse("ok", Collections.emptyList())
        ));
        when(database.getBufferTourID(userID)).thenReturn(3);
        when(database.tourOfferingFound(database.getBufferTourID(userID)
        		,Integer.parseInt(text))).thenReturn(true);
        when(database.setUserState(userID,Constant.BOOKING_ADULT)).thenReturn(true);
        when(database.deleteBufferBookingEntry(userID)).thenReturn(true);
        when(database.setBookingTourOfferingID(userID,Integer.parseInt(text))).thenReturn(true);
        
        underTest.BOOKING_OFFERING_ID_handler(replyToken, text, userID, "");

        // confirm replyMessage is called with following parameter
        verify(lineMessagingClient).replyMessage(new ReplyMessage(
                replyToken, singletonList(new TextMessage(expectReply))
        ));
    }
	
	@Test
    //for successful adult number
    public void test_BOOKING_ADULT_handler_success() throws Exception {

        String text = "12";
        String userID = "userId";
        String replyToken = "replyToken";
        String expectReply = Constant.INSTRTUCTION_ENTER_CHILDREN_NUMBER;

        // mock line bot api client response
        when(lineMessagingClient.replyMessage(new ReplyMessage(
                replyToken, singletonList(new TextMessage(expectReply))
        ))).thenReturn(CompletableFuture.completedFuture(
                new BotApiResponse("ok", Collections.emptyList())
        ));

        when(database.setUserState(userID,Constant.BOOKING_CHILDREN)).thenReturn(true);
        when(database.setBookingAdultNumber(userID,Integer.parseInt(text))).thenReturn(true);
        
        underTest.BOOKING_ADULT_handler(replyToken, text, userID, "");

        // confirm replyMessage is called with following parameter
        verify(lineMessagingClient).replyMessage(new ReplyMessage(
                replyToken, singletonList(new TextMessage(expectReply))
        ));
    }
	
	
	
	
	

}