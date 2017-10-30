/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import com.linecorp.bot.model.profile.UserProfileResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
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

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
@LineMessageHandler
public class KitchenSinkController {
	


	@Autowired
	private LineMessagingClient lineMessagingClient;

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		log.info("This is your entry point:");
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		TextMessageContent message = event.getMessage();
		handleTextContent(event.getReplyToken(), event, message);
	}

	@EventMapping
	public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
		handleSticker(event.getReplyToken(), event.getMessage());
	}

	@EventMapping
	public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
		LocationMessageContent locationMessage = event.getMessage();
		reply(event.getReplyToken(), new LocationMessage(locationMessage.getTitle(), locationMessage.getAddress(),
				locationMessage.getLatitude(), locationMessage.getLongitude()));
	}

	@EventMapping
	public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
		final MessageContentResponse response;
		String replyToken = event.getReplyToken();
		String messageId = event.getMessage().getId();
		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		DownloadedContent jpg = saveContent("jpg", response);
		reply(((MessageEvent) event).getReplyToken(), new ImageMessage(jpg.getUri(), jpg.getUri()));

	}

	@EventMapping
	public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
		final MessageContentResponse response;
		String replyToken = event.getReplyToken();
		String messageId = event.getMessage().getId();
		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		DownloadedContent mp4 = saveContent("mp4", response);
		reply(event.getReplyToken(), new AudioMessage(mp4.getUri(), 100));
	}

	@EventMapping
	public void handleUnfollowEvent(UnfollowEvent event) {
		log.info("unfollowed this bot: {}", event);
	}

	@EventMapping
	public void handleFollowEvent(FollowEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got followed event");
	}

	@EventMapping
	public void handleJoinEvent(JoinEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Joined " + event.getSource());
	}

	@EventMapping
	public void handlePostbackEvent(PostbackEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got postback " + event.getPostbackContent().getData());
	}

	@EventMapping
	public void handleBeaconEvent(BeaconEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got beacon message " + event.getBeacon().getHwid());
	}

	@EventMapping
	public void handleOtherEvent(Event event) {
		log.info("Received message(Ignored): {}", event);
	}

	private void reply(@NonNull String replyToken, @NonNull Message message) {
		reply(replyToken, Collections.singletonList(message));
	}

	private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
			log.info("Sent messages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private void replyText(@NonNull String replyToken, @NonNull String message) {
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken must not be empty");
		}
		//if (message.length() > 1000) {
			//message = message.substring(0, 1000 - 2) + "..";
		//}
		this.reply(replyToken, new TextMessage(message));
	}

	
	private boolean checkQuit(String text, String userID, int state, String reply, String replyToken) throws Exception{
		if (text.equals("Q")){
			database.setUserState(userID, state);
			database.deleteBookingEntry(userID);
			reply += "Successfully exiting booking!";
			log.info("Returns message {}: {}", replyToken, reply);
			this.replyText(replyToken,reply);
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isNumeric(String str)  
		 {  
		   try  
		   {  
		     int d = Integer.parseInt(str);  
		   }  
		   catch(NumberFormatException nfe)  
		   {  
		     return false;  
		   }  
		   return true;  
		 }


	private void handleSticker(String replyToken, StickerMessageContent content) {
		reply(replyToken, new StickerMessage(content.getPackageId(), content.getStickerId()));
	}

	private void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws Exception {
		
		// state constants
		
		// no user information
		final int FAQ1 = 100;
		
		// no confirmation but has user information
		final int FAQ2 = 300;
		
		// after confirmation
		final int FAQ3 = 500;
		
		// tell user to fill in his personal information
		final int FILL_INFORMATION = 200;
		
		final int PHONE_NUM = 201; 

		final int AGE = 202; 
		
		
		// tell user to provide booking information
		final int BOOKING = 400;
		
		final int BOOKING1 = 401;
		final int BOOKING2 = 402;
		final int BOOKING3 = 403;
		final int BOOKING4 = 404;
		final int BOOKING5 = 405;
		final int BOOKING6 = 406;
		

		
		// tell user to add a new booking or review
		final int ADD_BOOKING_OR_REVIEW = 600;
		
		
        String text = content.getText();
        log.info("Got text message from {}: {}", replyToken, text);
        
        java.sql.Timestamp time = new java.sql.Timestamp(new java.util.Date().getTime());
        String userID = event.getSource().getUserId();
        User user = database.getUserInformation(userID);
        String reply = "";
        if(user.getUserID().equals("null")) {
        		reply += "Thanks for your first use of our app! \n\n";      			
        		database.createUser(userID,time,FAQ1);
        		user.setID(userID);
        		user.setTime(time);
        		user.setState(FAQ1);
        }
        
        int state = user.getState();
        java.sql.Timestamp last_time = user.getTime();
        
        long difference = (time.getTime()-last_time.getTime())/(60*1000);
        
        // check whether the time gapping is larger than 10 minutes
        if(difference > 10) {
        		if (text=="hi") reply+="Hello!";
        		else {
        			
        			Calendar now = Calendar.getInstance();
        			int hour = now.get(Calendar.HOUR_OF_DAY);
        			
        	        if((hour+8)%24 < 12)
        	        		reply += "Good morning! \n";
        	        else if((hour+8)%24 >= 18) {
        	        		reply += "Good evening! \n";
        	        }else
        	        		reply += "Good afternoon \n";
        			
        		}
        }
        
        // update last_time
        database.setUserTime(userID,time);
        
        if(state == FAQ1 || state == FAQ2) {
        		// if the text does not indicate booking
        		if(!text.toLowerCase().contains("book")) {
        			faqsearch(replyToken, text, reply);
        		}
        		else {//indicate booking
        			if(state == FAQ1) {//no user info
        				database.setUserState(userID,FILL_INFORMATION);//set to 200
                		reply += "Thank you for your interest, we need some of your information. \n";
                		reply += "Please enter your name.";
                		log.info("Returns message {}: {}", replyToken, reply);
                		this.replyText(replyToken,reply);
         				
        			}else if(state == FAQ2)//
        				database.setUserState(userID,BOOKING);//set to 400 
            			reply += "Thank you for your interest, we need to fill in the booking information. \n";
            			reply += "Attention: You can terminate the booking procedure by entering 0 at any time!\n";
            			reply += "Here is a list of tour names: \n";
            			reply +=database.getTourNames();//String database.getTourNames();
            			reply +="\n";
            			reply +="Please enter one of the tour IDs.(Note: tourID only).  \n";
            			log.info("Returns message {}: {}", replyToken, reply);
            			this.replyText(replyToken,reply);
        		}
        	
        }
        else if(state == FILL_INFORMATION) {
        		database.setUserState(userID,PHONE_NUM);
        		// store the name info
        		database.setUserName(userID,text);
        		reply += "Please also give us your phone number. \n";
    			log.info("Returns message {}: {}", replyToken, reply);
    			this.replyText(replyToken,reply);
        
        	
        }else if(state == PHONE_NUM) {
        		database.setUserState(userID,AGE);
        		// store phone number
        		database.setUserPhoneNum(userID,text);
        		reply += "Please also give us your age. \n";
    			log.info("Returns message {}: {}", replyToken, reply);
    			this.replyText(replyToken,reply);

    			
      
        }else if(state == AGE) {
        		database.setUserAge(userID,text);//extract number preferred here
        		database.setUserState(userID,BOOKING);//enter booking, information filled
    			reply += "Great! Let's move on to booking your tour! \n";
    			reply +="Attention: You can terminate the booking procedure by entering 0 at any time!\n";
    			reply += "Here is a list of tour names: \n";
    			
    			String tourNames = database.getTourNames();//String database.getTourNames();
    			
    			//================
    			/*
    			String [] parts = tourNames.split("\n\n"); 
    			
    			int i = 0;
    			int count6 = 6;
    			String tours=" ";    			
    			while(i<parts.length){	
    				tours += parts[i];
    				tours +="\n";
    				if(count6!=0) {
    					count6--;
    				}
    				if(count6==0 ||i==parts.length-1) {
    					count6 = 6;
    	    				log.info("Returns message {}: {}", replyToken, reply);
    	    				this.replyText(replyToken,tours);
    	    				tours =" ";
    				}
    				i+=1;
    			}
    			*/
    			//=================
    			reply += tourNames;
    			reply +="\n";
    			reply +="Please enter one of the tourIDs (Note: tourID only). \n";
    			log.info("Returns message {}: {}", replyToken, reply);
    			this.replyText(replyToken,reply);	
        	
        }
        
        
        else if(state == BOOKING){
    			if (text.equals("Q")){
    				database.setUserState(userID, FAQ2);
    				reply += "Successfully exiting booking!";
    				log.info("Returns instruction message {}: {}", replyToken, reply);
    				this.replyText(replyToken,reply);
    			}
    			else if(isNumeric(text)) {
    				if(database.tourFound(Integer.parseInt(text))) {
         			String result = database.displayTourOffering(Integer.parseInt(text));
            			if(result.equals("null")) {
            				reply += "Sorry, currently we do not provide any offerings for this tour!\n"+
            						"Please choose another tour!";
            				log.info("Returns instruction message {}: {}", replyToken, reply);
                			this.replyText(replyToken,reply);			
            			}
            			else {
            				database.setUserState(userID,BOOKING1);
            				database.setBufferTourID(userID,Integer.parseInt(text));
            				reply += result;
            				reply += "Please enter one of the tour offering IDs. (Note: tour offering ID only).";
                			log.info("Returns instruction message {}: {}", replyToken, reply);
                			this.replyText(replyToken,reply);	
            			}    			
    				}
    				else {
    					reply += "Invalid tour ID! Please reinput tour ID.";
            			log.info("Returns instruction message {}: {}", replyToken, reply);
            			this.replyText(replyToken,reply);	
    				}
    			}
    			else {
    				reply += "Invalid tour ID! Please reinput tour ID.";
        			log.info("Returns instruction message {}: {}", replyToken, reply);
        			this.replyText(replyToken,reply);	
    			}
        }
        
        else if(state == BOOKING1){
			if (text.equals("Q")){
				database.setUserState(userID, FAQ2);
				database.deleteBufferBookingEntry(userID);
				reply += "Successfully exiting booking!";
				log.info("Returns message {}: {}", replyToken, reply);
				this.replyText(replyToken,reply);
			}
			
			else if(isNumeric(text)){
				if(database.tourOfferingFound(database.getBufferTourID(userID),Integer.parseInt(text))) {
					database.setUserState(userID,BOOKING2);
					database.deleteBufferBookingEntry(userID);
					database.setBookingTourOfferingID(userID,Integer.parseInt(text));
					reply += "Please input the number of adults for this tour offering.";
					log.info("Returns instruction message {}: {}", replyToken, reply);
	    				this.replyText(replyToken,reply);
				}
				else {
					reply += "Invalid tour offering ID! Please reinput tour offering ID.";
					log.info("Returns instruction message {}: {}", replyToken, reply);
					this.replyText(replyToken,reply);
				}
			}
			else {
				reply += "Invalid tour offering ID! Please reinput tour offering ID.";
				log.info("Returns instruction message {}: {}", replyToken, reply);
				this.replyText(replyToken,reply);
			}
		      		
        }
        
        else if(state == BOOKING2){
        		if(!checkQuit(text,userID,FAQ2,reply,replyToken)) {
        			if(isNumeric(text)) {
        				if(Integer.parseInt(text)>=0) {
        					database.setUserState(userID,BOOKING3);
        					database.setBookingAdultNumber(userID,Integer.parseInt(text));
        					reply += "Please input the number of childrens (age between 4 and 11) for this tour offering.";
        					log.info("Returns instruction message {}: {}", replyToken, reply);
        	    				this.replyText(replyToken,reply);
        				}
        				else {
        					reply += "Invalid number! Please reinput the number of adults.";
            				log.info("Returns instruction message {}: {}", replyToken, reply);
            				this.replyText(replyToken,reply);
        				}
        			}
        			else {
        				reply += "Invalid number! Please reinput the number of adults.";
        				log.info("Returns instruction message {}: {}", replyToken, reply);
        				this.replyText(replyToken,reply);
        			}
        			
        		}
   		
        }
        
        else if(state == BOOKING3){
    		if(!checkQuit(text,userID,FAQ2,reply,replyToken)) {
    			if(isNumeric(text)) {
    				if(Integer.parseInt(text)>=0) {
    					database.setUserState(userID,BOOKING4);
    					database.setBookingChildrenNumber(userID,Integer.parseInt(text));
    					reply += "Please input the number of toddlers (age not larger than 3) for this tour offering.";
    					log.info("Returns instruction message {}: {}", replyToken, reply);
    	    				this.replyText(replyToken,reply);
    				}
    				else {
    					reply += "Invalid number! Please reinput the number of children.";
        				log.info("Returns instruction message {}: {}", replyToken, reply);
        				this.replyText(replyToken,reply);
    				}
    			}
    			else {
    				reply += "Invalid number! Please reinput the number of children.";
    				log.info("Returns instruction message {}: {}", replyToken, reply);
    				this.replyText(replyToken,reply);
    			}
    			
    		}
        }
        
        	else if(state == BOOKING4){
        		if(!checkQuit(text,userID,FAQ2,reply,replyToken)) {
        			if(isNumeric(text)) {
        				if(Integer.parseInt(text)>=0) {
        					database.setUserState(userID,BOOKING5);
        					database.setBookingToddlerNumber(userID,Integer.parseInt(text));
        					reply += "Please leave your special request.";
        					log.info("Returns instruction message {}: {}", replyToken, reply);
        	    				this.replyText(replyToken,reply);
        				}
        				else {
        					reply += "Invalid number! Please reinput the number of toddlers.";
            				log.info("Returns instruction message {}: {}", replyToken, reply);
            				this.replyText(replyToken,reply);
        				}
        			}
        			else {
        				reply += "Invalid number! Please reinput the number of toddlers.";
        				log.info("Returns instruction message {}: {}", replyToken, reply);
        				this.replyText(replyToken,reply);
        			}
        			
        		}
		}
        
        else if(state == BOOKING5){
    			if(!checkQuit(text,userID,FAQ2,reply,replyToken)) {
    				database.setUserState(userID,BOOKING6);
    				database.setBookingSpecialRequest(userID,text);
    				reply += database.displaytBookingInformation(userID);	
    				reply += ("Do you want to confirm your booking? \n"+"(yes/no)");
    				log.info("Returns instruction message {}: {}", replyToken, reply);
    				this.replyText(replyToken,reply);
    			}
        }
        
        else if(state == BOOKING6){
    			if(!checkQuit(text,userID,FAQ2,reply,replyToken)) {
    				if(text.toLowerCase().contains("y")) {
    					database.setUserState(userID,FAQ3);
    					database.setBookingConfirmation(userID);
    					reply += ("Thanks for your interest! \n"
    							+ "Please pay your tour fee by ATM to 123-345-432-211 of ABC Bank or by cash in our store.\n"
    							+ "You shall send you pay-in slip to us by email or LINE. \n"
    							+ "You are welcome to ask other questions!");
    		    			log.info("Returns instruction message {}: {}", replyToken, reply);
    		    			this.replyText(replyToken,reply);    			
    				}
    				else {
    					database.setUserState(userID,FAQ2);
    					database.deleteBookingEntry(userID);
    					reply += "Booking cancelled!";
    		    			log.info("Returns message {}: {}", replyToken, reply);
    		    			this.replyText(replyToken,reply);
    				}
    			}
        }
        
        else if(state==FAQ3){
            if(!text.toLowerCase().contains("book")) {
    			faqsearch(replyToken, text, reply);   
            }
            else{
                database.setUserState(userID,ADD_BOOKING_OR_REVIEW);
                reply += "Do you want to review your previous booking or do you want to start a new book?\n";
                reply += "(review/new booking)";
                log.info("Returns message {}: {}", replyToken, reply);
                this.replyText(replyToken,reply);    
             }   
        }
        
        else if(state==ADD_BOOKING_OR_REVIEW){
               if(text.toLowerCase().contains("review")) {
            	   	database.setUserState(userID,FAQ3);
            	   	reply += database.reviewBookingInformation(userID);
            	   	log.info("Returns message {}: {}", replyToken, reply);
            	   	this.replyText(replyToken,reply);      
               }
               else {
            	   	database.setUserState(userID,BOOKING);
            	   	reply += "Thank you for your interest, we need to fill in the booking information. \n";
            	   	reply += "Attention: You can terminate the booking procedure by entering 0 at any time!\n";
            	   	reply += "Here is a list of tour names: \n";
            	   	reply +=database.getTourNames();//String database.getTourNames();
            	   	reply +="\n";
            	   	reply +="Please enter one of the tour IDs.(Note: tourID only).  \n";
            	   	log.info("Returns message {}: {}", replyToken, reply);
            	   	this.replyText(replyToken,reply);
                
               }
       }        
        
        /*
        else if(state==FAQ3){
            if(!text.toLowerCase().contains("book")) {
                String answer = database.search(text);
                if(!answer.equals("null")) {
                 log.info("Returns answer message {}: {}", replyToken, answer);
                 this.replyText(replyToken,answer);
                }else {
             String reply = "Sorry! We cannot answer your question.";
             //.unanswered question, add to unknown question database
             database.addToUnknownDatatabse(text);
                   log.info("Returns message {}: {}", replyToken, reply);
                   this.replyText(replyToken,reply);
                }      
               }else {
                database.setUserState(userID,ADD_BOOKING_OR_REVIEW);
                String reply = "Do you want to review your previous booking or do you want to start a new book?\n";
                reply += "answer: review /  new booking ";
                   log.info("Returns message {}: {}", replyToken, reply);
                   this.replyText(replyToken,reply);    
               }   
             }else if(state==ADD_BOOKING_OR_REVIEW){
               if(text.toLowerCase().contains("review")) {
                database.setUserState(userID,FAQ3);
                String reply = database.displaytBookingInformation(userID);
                log.info("Returns message {}: {}", replyToken, reply);
                this.replyText(replyToken,reply);    
       
                
               }else {
                database.setUserState(userID,BOOKING);
                String reply = "Thank you for your interest, we need to fill in the booking information. \n";
                reply += "Attention: You can terminate the booking procedure by entering 0 at any time!\n";
                reply += "Here is a list of tour names: \n";
                reply +=database.getTourNames();//String database.getTourNames();
                reply +="\n";
                reply +="Please enter one of the tour IDs.(Note: tourID only).  \n";
                log.info("Returns message {}: {}", replyToken, reply);
                this.replyText(replyToken,reply);
                
               }
             }

        */
        
			
		
        
        
        
        /*
        switch (text) {
            case "profile": {
                String userId = event.getSource().getUserId();
                if (userId != null) {
                    lineMessagingClient
                            .getProfile(userId)
                            .whenComplete(new ProfileGetter (this, replyToken));
                } else {
                    this.replyText(replyToken, "Bot can't use profile API without user ID");
                }
                break;
            }
            case "confirm": {
                ConfirmTemplate confirmTemplate = new ConfirmTemplate(
                        "Do it?",
                        new MessageAction("Yes", "Yes!"),
                        new MessageAction("No", "No!")
                );
                TemplateMessage templateMessage = new TemplateMessage("Confirm alt text", confirmTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "carousel": {
                String imageUrl = createUri("/static/buttons/1040.jpg");
                CarouselTemplate carouselTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new URIAction("Go to line.me",
                                                      "https://line.me"),
                                        new PostbackAction("Say hello1",
                                                           "hello é‘¼î‚¦æ•“ç‘™ï½�æ‹·å©§å¶�çµºé”ŸèŠ¥åŸƒé”ŸèŠ¥ç°«é”�å¿”å«¹é‘ºï¹�å°—é–¿ç†¼æ‘ªéˆ­è®¹ç¶‡éŽ·é£Žå€�")
                                )),
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new PostbackAction("é�šî‚¤ç˜¬é–³Ñ�æ‹· hello2",
                                                           "hello é‘¼î‚¦æ•“ç‘™ï½�æ‹·å©§å¶�çµºé”ŸèŠ¥åŸƒé”ŸèŠ¥ç°«é”�å¿”å«¹é‘ºï¹�å°—é–¿ç†¼æ‘ªéˆ­è®¹ç¶‡éŽ·é£Žå€�",
                                                           "hello é‘¼î‚¦æ•“ç‘™ï½�æ‹·å©§å¶�çµºé”ŸèŠ¥åŸƒé”ŸèŠ¥ç°«é”�å¿”å«¹é‘ºï¹�å°—é–¿ç†¼æ‘ªéˆ­è®¹ç¶‡éŽ·é£Žå€�"),
                                        new MessageAction("Say message",
                                                          "Rice=é‘¾è—‰å´µæ¤´ï¿½")
                                ))
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }

            default:
            	String reply = null;
            	try {
            		reply = database.search(text);
            	} catch (Exception e) {
            		reply = text;
            	}
                log.info("Returns echo message {}: {}", replyToken, reply);
                this.replyText(
                        replyToken,
                        itscLOGIN + " says " + reply
                );
                break;
        }
        */
    }

	private void faqsearch(String replyToken, String text, String reply) throws Exception {
		try {
		String answer = faqDatabase.search(text);
		reply += answer;
		String imageURL=faqDatabase.replyImage(answer);
		if (imageURL!=null) {
			imageURL=createUri("static/pictures/"+imageURL);
			this.reply(replyToken, Arrays.asList(new TextMessage(reply),new ImageMessage(imageURL, imageURL)));
			log.info("Replied image message {}: {}", replyToken, reply);

		}
		
		log.info("Returns answer message {}: {}", replyToken, reply);
		this.replyText(replyToken,reply);
		}catch(Exception e) {
			reply += "Sorry! We don't have relevant information.";
			//.unanswered question, add to unknown question database
			database.addToUnknownDatatabse(text);
			log.info("Returns message {}: {}", replyToken, reply);
			this.replyText(replyToken,reply);       				
			
		}
	}

	static String createUri(String path) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUriString();
	}

	private void system(String... args) {
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		try {
			Process start = processBuilder.start();
			int i = start.waitFor();
			log.info("result: {} =>  {}", Arrays.toString(args), i);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {
			log.info("Interrupted", e);
			Thread.currentThread().interrupt();
		}
	}

	private static DownloadedContent saveContent(String ext, MessageContentResponse responseBody) {
		log.info("Got content-type: {}", responseBody);

		DownloadedContent tempFile = createTempFile(ext);
		try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
			ByteStreams.copy(responseBody.getStream(), outputStream);
			log.info("Saved {}: {}", ext, tempFile);
			return tempFile;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static DownloadedContent createTempFile(String ext) {
		String fileName = LocalDateTime.now().toString() + '-' + UUID.randomUUID().toString() + '.' + ext;
		Path tempFile = KitchenSinkApplication.downloadedContentDir.resolve(fileName);
		tempFile.toFile().deleteOnExit();
		return new DownloadedContent(tempFile, createUri("/downloaded/" + tempFile.getFileName()));
	}


	


	public KitchenSinkController() {
		database = new SQLDatabaseEngine();
		faqDatabase = new FaqDatabase();
		itscLOGIN = System.getenv("ITSC_LOGIN");
	}

	private SQLDatabaseEngine database;
	private FaqDatabase faqDatabase;
	private String itscLOGIN;
	

	//The annontation @Value is from the package lombok.Value
	//Basically what it does is to generate constructor and getter for the class below
	//See https://projectlombok.org/features/Value
	@Value
	public static class DownloadedContent {
		Path path;
		String uri;
	}


	//an inner class that gets the user profile and status message
	class ProfileGetter implements BiConsumer<UserProfileResponse, Throwable> {
		private KitchenSinkController ksc;
		private String replyToken;
		
		public ProfileGetter(KitchenSinkController ksc, String replyToken) {
			this.ksc = ksc;
			this.replyToken = replyToken;
		}
		@Override
    	public void accept(UserProfileResponse profile, Throwable throwable) {
    		if (throwable != null) {
            	ksc.replyText(replyToken, throwable.getMessage());
            	return;
        	}
        	ksc.reply(
                	replyToken,
                	Arrays.asList(new TextMessage(
                		"Display name: " + profile.getDisplayName()),
                              	new TextMessage("Status message: "
                            		  + profile.getStatusMessage()))
        	);
    	}
    }
	
	

}
