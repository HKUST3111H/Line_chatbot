package com.example.bot.spring;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
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
        when(lineMessagingClient.replyMessage(new ReplyMessage(
                replyToken, singletonList(new TextMessage(expectReply))
        ))).thenReturn(CompletableFuture.completedFuture(
                new BotApiResponse("ok", Collections.emptyList())
        ));

        when(database.setUserState(userId, Constant.FILL_NAME)).thenReturn(true);

        underTest.FAQ_NO_USER_INFORMATION_handler(replyToken, testMsg, userId, "");

        // confirm replyMessage is called with following parameter
        verify(lineMessagingClient).replyMessage(new ReplyMessage(
                replyToken, singletonList(new TextMessage(expectReply))
        ));
    }
}