package com.example.bot.spring;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

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
	public void test_BOOKING_CONFIRMATION_handler_not_quit() throws Exception {
		String text = "special request";
		String userID = "userID";
		String replyToken = "replyToken";
		String reply = "";

		ConfirmTemplate confirmTemplate = new ConfirmTemplate(Constant.QUESTION_CONFIRM_OR_NOT,
				new MessageAction("Yes", "Yes!"), new MessageAction("No", "No!"));
		TemplateMessage confirmMessageBlock = new TemplateMessage("Confirm booking?", confirmTemplate);
		List<Message> obj = new ArrayList<Message>();
		obj.add(new TextMessage(reply));
		obj.add(confirmMessageBlock);
		when(lineMessagingClient.replyMessage(new ReplyMessage(replyToken, obj)))
				.thenReturn(CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));

		when(database.setUserState(userID, Constant.BOOKING_PAYMENT)).thenReturn(true);
		when(database.setBookingSpecialRequest(userID, text)).thenReturn(true);
		when(database.displaytBookingInformation(userID)).thenReturn("");

		underTest.BOOKING_CONFIRMATION_handler(replyToken, text, userID, reply);

		verify(lineMessagingClient).replyMessage(new ReplyMessage(replyToken, obj));

	}

	@Test
	public void test_BOOKING_CONFIRMATION_handler_quit() throws Exception {
		String text = "Q";
		String userID = "userID";
		String replyToken = "replyToken";
		String reply = "";
		when(database.reviewBookingInformation(userID)).thenReturn("null");
		when(database.setUserState(userID, Constant.FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION)).thenReturn(true);
		when(database.deleteBookingEntry(userID)).thenReturn(true);
		when(lineMessagingClient.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(Constant.CANCEL)))))
				.thenReturn(CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		underTest.BOOKING_CONFIRMATION_handler(replyToken, text, userID, reply);
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(Constant.CANCEL))));
	}

	@Test
	public void test_faqsearch_catch() throws Exception {

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

	/*
	 * @Test public void test_faqsearch_image() throws Exception { String text =
	 * "where is the gathering point"; String userID =
	 * "U52a29b672ee486b66b7fb4c45a888de3"; String replyToken = "replyToken"; String
	 * reply = ""; boolean thrown = false;
	 * 
	 * try { FaqDatabase faq = new FaqDatabase(); String path =
	 * faq.replyImage(faq.search(text, userID)); //
	 * System.out.println("\n\n\n"+path+"\n\n\n"); String url =
	 * LineMessageController.createUri("static/pictures/"+path);
	 * when(lineMessagingClient .replyMessage(new ReplyMessage(replyToken,
	 * Arrays.asList(new TextMessage(faq.search(text, userID)), new
	 * ImageMessage(any(String.class), any(String.class)))))).thenReturn(
	 * CompletableFuture.completedFuture(new BotApiResponse("ok",
	 * Collections.emptyList()))); underTest.faqsearch(replyToken, text, reply,
	 * userID); verify(lineMessagingClient) .replyMessage(new
	 * ReplyMessage(replyToken, Arrays.asList(new TextMessage(faq.search(text,
	 * userID)), new ImageMessage(any(String.class), any(String.class))))); }
	 * catch(Exception e) { System.out.println(e.toString()); thrown = true; }
	 * assertThat(thrown).isEqualTo(false); }
	 */

	@Test
	public void test_faqsearch_normal() throws Exception {
		String text = "hello";
		String userID = "U52a29b672ee486b66b7fb4c45a888de3";
		String replyToken = "replyToken";
		String reply = "";
		when(lineMessagingClient.replyMessage(
				new ReplyMessage(replyToken, singletonList(new TextMessage("Hi! Do you have any question?")))))
						.thenReturn(
								CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		underTest.faqsearch(replyToken, text, reply, userID);
		verify(lineMessagingClient).replyMessage(
				new ReplyMessage(replyToken, singletonList(new TextMessage("Hi! Do you have any question?"))));

	}

	@Test
	public void test_FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION_handler() throws Exception {
		String text = "hello";
		String userID = "U52a29b672ee486b66b7fb4c45a888de3";
		String replyToken = "replyToken";
		String reply = "";
		when(lineMessagingClient.replyMessage(
				new ReplyMessage(replyToken, singletonList(new TextMessage("Hi! Do you have any question?")))))
						.thenReturn(
								CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		underTest.FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION_handler(replyToken, text, userID, reply);
		// when(database.setUserName(userId, testMsg)).thenReturn(true);

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient).replyMessage(
				new ReplyMessage(replyToken, singletonList(new TextMessage("Hi! Do you have any question?"))));
	}

	@Test
	public void test_FAQ_AFTER_CONFIRMATION_handler() throws Exception {
		String text = "hello";
		String userID = "U52a29b672ee486b66b7fb4c45a888de3";
		String replyToken = "replyToken";
		String reply = "";
		when(lineMessagingClient.replyMessage(
				new ReplyMessage(replyToken, singletonList(new TextMessage("Hi! Do you have any question?")))))
						.thenReturn(
								CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		underTest.FAQ_AFTER_CONFIRMATION_handler(replyToken, text, userID, reply);
		// when(database.setUserName(userId, testMsg)).thenReturn(true);

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient).replyMessage(
				new ReplyMessage(replyToken, singletonList(new TextMessage("Hi! Do you have any question?"))));

	}

	@Test
	public void test_FAQ_AFTER_CONFIRMATION_handler_book() throws Exception {
		String text = "book";
		String userID = "U52a29b672ee486b66b7fb4c45a888de3";
		String replyToken = "replyToken";
		String reply = "";
		ConfirmTemplate confirmTemplate = new ConfirmTemplate(Constant.QUESTION_REVIEW_OR_BOOKING,
				new MessageAction("Review", "Review"),
				new MessageAction(Constant.TEXT_NEW_BOOKING, Constant.TEXT_NEW_BOOKING));
		TemplateMessage whichBook = new TemplateMessage("Review Booking/New Booking", confirmTemplate);
		when(lineMessagingClient.replyMessage(new ReplyMessage(replyToken, singletonList(whichBook))))
				.thenReturn(CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		underTest.FAQ_AFTER_CONFIRMATION_handler(replyToken, text, userID, reply);
		when(database.setUserState(userID, Constant.BOOKING_OR_REVIEW)).thenReturn(true);

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient).replyMessage(new ReplyMessage(replyToken, singletonList(whichBook)));

	}

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
	public void test_FILL_AGE_handler_invalid_nontext() throws Exception {

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

	@Test
	public void test_FILL_AGE_handler_invalid_smaller_than_zero() throws Exception {

		String testMsg = "-2";
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
		underTest.BOOKING_OFFERING_ID_handler(replyToken, text, userID, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}

	@Test
	// for successful offering id
	public void test_BOOKING_OFFERING_ID_handler_success() throws Exception {

		String text = "12";
		String userID = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.INSTRTUCTION_ENTER_ADULT_NUMBER;

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		when(database.getBufferTourID(userID)).thenReturn(3);
		when(database.tourOfferingFound(database.getBufferTourID(userID), Integer.parseInt(text))).thenReturn(true);
		when(database.setUserState(userID, Constant.BOOKING_ADULT)).thenReturn(true);
		when(database.deleteBufferBookingEntry(userID)).thenReturn(true);
		when(database.setBookingTourOfferingID(userID, Integer.parseInt(text))).thenReturn(true);

		underTest.BOOKING_OFFERING_ID_handler(replyToken, text, userID, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}

	@Test
	// for successful adult number
	public void test_BOOKING_ADULT_handler_success() throws Exception {

		String text = "12";
		String userID = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.INSTRTUCTION_ENTER_CHILDREN_NUMBER;

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));

		when(database.setUserState(userID, Constant.BOOKING_CHILDREN)).thenReturn(true);
		when(database.setBookingAdultNumber(userID, Integer.parseInt(text))).thenReturn(true);

		underTest.BOOKING_ADULT_handler(replyToken, text, userID, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}

	@Test
	public void test_BOOKING_PAYMENT_handler() throws Exception {

		String testMsg = "yes";
		String userID = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.INSTRUCTION_PAYMENT;
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));

		when(database.setUserState(userID, Constant.FAQ_AFTER_CONFIRMATION)).thenReturn(true);
		when(database.setBookingConfirmation(userID)).thenReturn(true);

		underTest.BOOKING_PAYMENT_handler(replyToken, testMsg, userID, "");

		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}
}