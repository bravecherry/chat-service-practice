package org.example.fastcampus.dtos;

import org.example.fastcampus.entities.Message;

public record ChatMessage(String sender, String message) {

    public static ChatMessage from(Message message) {
        return new ChatMessage(message.getMember().getNickName(), message.getText());
    }

}
