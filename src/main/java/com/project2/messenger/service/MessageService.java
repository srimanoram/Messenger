package com.project2.messenger.service;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.Message;
import com.project2.messenger.model.User;
import com.project2.messenger.model.records.ChatHistory;
import com.project2.messenger.repository.ChatMemberRepository;
import com.project2.messenger.repository.MessageRepository;
import com.project2.messenger.serviceUtil.ServiceUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;
    private static final int pageSize = 5;

    public MessageService(MessageRepository messageRepository,  ChatMemberRepository chatMemberRepository) {
        this.messageRepository = messageRepository;
        this.chatMemberRepository = chatMemberRepository;
    }


    public Message sendMessage(Chat chat, String content)  {
        User user = ServiceUtil.getLoggedInUser();
        if(!chatMemberRepository.existsByChatAndUser(chat, user)) {
            throw new AccessDeniedException("User is not a member of the chat");
        }

        Message message = new Message();
        message.setContent(content);
        message.setChat(chat);
        message.setSender(user);
        messageRepository.save(message);
        return message;
    }

    /*public ChatHistory loadChatMessages(Chat chat) {
        return this.loadChatMessages(chat, Long.MAX_VALUE);
    }*/

    public ChatHistory loadChatMessages(Long chatId, Long beforeId) {
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Message> messages = messageRepository.findByChat_IdAndIdLessThanOrderByIdDesc(chatId, beforeId, pageable);
        Long cursorId = 0L;
        if(!messages.isEmpty())
            cursorId = messages.getLast().getId();
        return new ChatHistory(messages, cursorId, messages.size() == pageSize);

    }

}
