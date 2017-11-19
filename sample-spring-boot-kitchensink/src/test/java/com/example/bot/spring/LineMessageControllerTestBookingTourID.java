package com.example.bot.spring;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.Ignore;
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
import com.linecorp.bot.model.event.source.UserSource;
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

public class LineMessageControllerTestBookingTourID {

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
	//for checkQuit==ture;
	public void test_BOOKING_TOUR_ID_handler_Quit() throws Exception {

		String text = "Q";//
		String userID = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.CANCEL;

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		when(database.reviewBookingInformation(userID)).thenReturn("null");
		
		
		
		
		underTest.BOOKING_TOUR_ID_handler(replyToken, text, userID, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}
	
	
	@Test
	//for checkQuit==false; several cases below
	//case 1: input is not an integer tour ID
	public void test_BOOKING_TOUR_ID_handler_NotQuit_Case1() throws Exception {

		String text = "george";
		String userID = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.ERROR_REENTER_TOUR_ID;
		List<Tour> listOfTours = new ArrayList<Tour>();
		listOfTours.add(new Tour(100,"suzhou","nice city",5,"suzhou.jpeg","nice"));

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		when(database.getTours()).thenReturn(listOfTours);
		
		
		
		underTest.BOOKING_TOUR_ID_handler(replyToken, text, userID, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}
	
	
	@Test
	//for checkQuit==false; several cases below
	//case 2: tour offering id NOT available 
	public void test_BOOKING_TOUR_ID_handler_NotQuit_Case2() throws Exception {

		String text = "6";//non int tour id
		String userID = "userId";
		String replyToken = "replyToken";
		String expectReply = Constant.ERROR_REENTER_TOUR_ID;
		List<Tour> listOfTours = new ArrayList<Tour>();
		listOfTours.add(new Tour(100,"suzhou","nice city",5,"suzhou.jpeg","nice"));

		// mock line bot api client response
		when(lineMessagingClient
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))))).thenReturn(
						CompletableFuture.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
		when(database.getTours()).thenReturn(listOfTours);
		when(database.tourFound(Integer.parseInt(text))).thenReturn(false);
		underTest.BOOKING_TOUR_ID_handler(replyToken, text, userID, "");

		// confirm replyMessage is called with following parameter
		verify(lineMessagingClient)
				.replyMessage(new ReplyMessage(replyToken, singletonList(new TextMessage(expectReply))));
	}
	
	
}