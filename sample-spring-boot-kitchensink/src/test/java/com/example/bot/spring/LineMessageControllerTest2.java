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
public class LineMessageControllerTest2 {

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
	public void test_welcome_back() throws Exception {
		User user = new User();
		underTest.welcomeBack(11, user);
		user.setName("not null");
		underTest.welcomeBack(11, user);
	}

	@Test
	public void test_FILL_AGE_handler_booking() throws Exception{
		
		String testMsg = "30";
		String userID = "userId";
		String replyToken = "replyToken";
		String reply = "";
		List<Message> expectReply = new ArrayList<Message>();

		when(database.setUserAge(userID, testMsg)).thenReturn(true);
		when(database.setUserState(userID, Constant.BOOKING_TOUR_ID)).thenReturn(true);
		expectReply.add(new TextMessage(Constant.INSTRUCTION_BOOKING));
		expectReply.add(new TextMessage("No Tours Avaliable"));

		when(database.getTours()).thenThrow(new Exception("EMPTY DATABASE MOCK"));

        when(lineMessagingClient.replyMessage(new ReplyMessage(
                replyToken, expectReply
        ))).thenReturn(CompletableFuture.completedFuture(
                new BotApiResponse("ok", Collections.emptyList())
		));

		underTest.FILL_AGE_handler(replyToken, testMsg, userID, "");
		verify(lineMessagingClient).replyMessage(new ReplyMessage(replyToken, expectReply));
	}

	@Test
	public void test_replyText(){
		String replyToken = "";
		try {
			underTest.replyText(replyToken, "");
		}
		catch (Exception e) {
			// log.info(e.toString());
		}

	}
	
	// @Test
	// public void test_reply() throws Exception{
	// 	String replyToken = "replyToken";
	// 	String testMsg = "message";
	// 	String expectReply = "expectReply";

    //     when(lineMessagingClient.replyMessage(new ReplyMessage(
    //             replyToken, expectReply
    //     ))).thenReturn(CompletableFuture.completedFuture(
    //             new BotApiResponse("ok", Collections.emptyList())
	// 	));

	// 	underTest.replyText(replyToken, "");
	// 	verify(lineMessagingClient).replyMessage(new ReplyMessage(replyToken, expectReply));
	// }
	
}