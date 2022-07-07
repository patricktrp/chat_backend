package com.patricktreppmann.chatserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Service
public class ChatService {
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final ChatRepository chatRepository;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final MessageListenerAdapter messageListenerAdapter;

    public ChatService(ChatRepository chatRepository, RedisMessageListenerContainer redisMessageListenerContainer, MessageListenerAdapter messageListenerAdapter) {
        this.chatRepository = chatRepository;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.messageListenerAdapter = messageListenerAdapter;
    }

    @EventListener
    public void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
        logger.info("client unsubscribed");
        System.out.println(event);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        logger.info("client disconnected");
        String sessionId = event.getSessionId();
        chatRepository.delete(sessionId);
        chatRepository.printMap();
    }

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        logger.info("client connected");
        System.out.println(event);
    }

    @EventListener
    public void handleSessionSubscribe(SessionSubscribeEvent event) {
        logger.info("client subscribed");
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        String chatroomId = (String) event.getMessage().getHeaders().get("simpDestination");

        String[] x = chatroomId.split("/", 3);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new ChannelTopic(x[2]));

        chatRepository.add(x[2], sessionId);
        chatRepository.printMap();
    }
}
