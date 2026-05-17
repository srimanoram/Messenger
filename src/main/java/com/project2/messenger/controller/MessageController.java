package com.project2.messenger.controller;

import com.project2.messenger.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    public void sendMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/message", message);
    }

    @MessageMapping("/private")
    public void sendPrivateMessage(ChatMessage message) {
        messagingTemplate.convertAndSendToUser(message.to() ,"/queue/message", message);
    }
}
