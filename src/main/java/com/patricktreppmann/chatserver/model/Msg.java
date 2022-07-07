package com.patricktreppmann.chatserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Msg {
    private String message;
    private String chatRoom;

    @Override
    public String toString() {
        return "Msg{" +
                "message='" + message + '\'' +
                ", chatRoom='" + chatRoom + '\'' +
                '}';
    }
}
