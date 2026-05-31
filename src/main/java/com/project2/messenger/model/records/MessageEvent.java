package com.project2.messenger.model.records;

import com.project2.messenger.model.Message;

import java.time.LocalDateTime;

public record MessageEvent(Long id, Long chatId, String senderUsername, String content, LocalDateTime sentTime) {

    public static MessageEvent from(Message m) {
        return new MessageEvent(
                m.getId(),
                m.getChat().getId(),
                m.getSender().getUsername(),
                m.getContent(),
                m.getSentTime()
        );
    }
}