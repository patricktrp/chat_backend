package com.patricktreppmann.chatserver.redis.publisher;

import com.patricktreppmann.chatserver.model.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    private final RedisTemplate<String,Object> template;

    @Autowired
    public Publisher(RedisTemplate<String,Object> template) {
        this.template = template;
    }

    public void publish(String channelName, String message) {
        template.convertAndSend(channelName, message);
    }
}
