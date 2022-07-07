package com.patricktreppmann.chatserver.controller;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatRepository {
    private final Map<String, Set<String>> chatRooms;

    public ChatRepository() {
        this.chatRooms = new HashMap<>();
    }

    public void add(String chatroomId, String sessionId) {
        if (chatRooms.containsKey(chatroomId)) {
            chatRooms.get(chatroomId).add(sessionId);
        } else {
            Set<String> med = new HashSet<>();
            med.add(sessionId);
            chatRooms.put(chatroomId, med);
        }
    }

    public void delete(String sessionId) {
        for (Map.Entry<String, Set<String>> entry : chatRooms.entrySet()) {
            String key = entry.getKey();
            Set<String> value = entry.getValue();
            if (value.contains(sessionId)) {
                value.remove(sessionId);
                if (value.isEmpty()) {
                    chatRooms.remove(key);
                }
                break;
            }
        }
    }

    public void printMap() {
        for (String x : chatRooms.keySet()) {
            System.out.printf("%s: %s\n",x,chatRooms.get(x));
        }
    }
}
