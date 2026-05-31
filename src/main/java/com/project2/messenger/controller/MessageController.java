package com.project2.messenger.controller;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.Message;
import com.project2.messenger.model.records.MessageEvent;
import com.project2.messenger.repository.ChatMemberRepository;
import com.project2.messenger.service.ChatService;
import com.project2.messenger.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageService messageService;
    private final ChatService chatService;
    private final ChatMemberRepository chatMemberRepository;

    public MessageController(SimpMessagingTemplate messagingTemplate, MessageService messageService, ChatService chatService, ChatMemberRepository chatMemberRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.chatService = chatService;
        this.chatMemberRepository = chatMemberRepository;
    }

    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@DestinationVariable Long chatId, @Payload String message){
        Chat chat = chatService.findChatById(chatId);
        Message saved = messageService.sendMessage(chat, message);

        MessageEvent event = MessageEvent.from(saved);

        chatMemberRepository.findByChat(chat).forEach(chatMember -> {
            messagingTemplate.convertAndSendToUser(chatMember.getUser().getUsername(), "/queue/messages", event);
        });
    }




   /* @MessageMapping("/message")
    public void sendMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/message", message);
    }

    @MessageMapping("/private")
    public void sendPrivateMessage(ChatMessage message) {
        messagingTemplate.convertAndSendToUser(message.to() ,"/queue/message", message);
    }*/
}
