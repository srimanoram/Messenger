package com.project2.messenger.service;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.Message;
import com.project2.messenger.model.records.ChatHistory;
import com.project2.messenger.repository.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private static final int pageSize = 5;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public

    /*public ChatHistory loadChatMessages(Chat chat) {
        return this.loadChatMessages(chat, Long.MAX_VALUE);
    }*/

    public ChatHistory loadChatMessages(Chat chat, Long beforeId) {

        Pageable pageable = PageRequest.of(0, pageSize+1);
        List<Message> messages = messageRepository.findByChatAndIdLessThanOrderByIdDesc(chat, beforeId, pageable);
        Long cursorId = 0L;
        if(!messages.isEmpty())
            cursorId = messages.getLast().getId();
        return new ChatHistory(messages, cursorId, messages.size() == pageSize);

    }

}
