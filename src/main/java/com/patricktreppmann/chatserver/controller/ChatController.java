package com.patricktreppmann.chatserver.controller;

import com.patricktreppmann.chatserver.redis.publisher.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class ChatController {
    @Value("${APPID:1}")
    private String appId;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final Publisher publisher;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, Publisher publisher) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.publisher = publisher;
    }

    @MessageMapping("/message/{channelName}")
    @SendTo("/chatroom/{channelName}")
    private String receivePublicMessage(@Payload String message, @DestinationVariable String channelName) {
        publisher.publish(channelName, message);
        return message;
    }

    @GetMapping("/create-chatroom")
    @ResponseBody
    @CrossOrigin
    public String createChatroom() {
        return UUID.randomUUID().toString();
    }

    @GetMapping("/appid")
    @ResponseBody
    @CrossOrigin
    public String getAppId() {
        return appId;
    }

    public String sendOutMessage(String channelName, String message) {
        simpMessagingTemplate.convertAndSend("/chatroom/"+channelName, message);
        return "hi";
    }
}
