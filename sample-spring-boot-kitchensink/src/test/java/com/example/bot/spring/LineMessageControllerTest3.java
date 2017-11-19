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
//@Ignore
public class LineMessageControllerTest3 {

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
	public void test_greeting() throws Exception {
		String result = underTest.greeting();
		assertThat(result.contains("Good")).isEqualTo(true);
	}

	@Test
	public void test_faqsearch_image() throws Exception {
		String text = "where is the gathering point";
		String userID = "U52a29b672ee486b66b7fb4c45a888de3";
		String replyToken = "replyToken";
		String reply = "";
		boolean thrown = false;

		try {
			FaqDatabase faq = new FaqDatabase();
			String path = faq.replyImage(faq.search(text, userID)); //
			System.out.println("\n\n\n" + path + "\n\n\n");
			String url = LineMessageController.createUri(path);
			when(lineMessagingClient.replyMessage(new ReplyMessage(replyToken,
					Arrays.asList(new TextMessage(faq.search(text, userID)),
							new ImageMessage(url, url)))))
									.thenReturn(CompletableFuture
											.completedFuture(new BotApiResponse("ok", Collections.emptyList())));
			underTest.faqsearch(replyToken, text, reply, userID);
			verify(lineMessagingClient)
					.replyMessage(new ReplyMessage(replyToken, Arrays.asList(new TextMessage(faq.search(text, userID)),
							new ImageMessage(url, url))));
		} catch (Exception e) {
			System.out.println(e.toString());
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
	}

}
