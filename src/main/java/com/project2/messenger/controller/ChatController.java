package com.project2.messenger.controller;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.User;
import com.project2.messenger.model.records.ChatHistory;
import com.project2.messenger.model.records.ChatView;
import com.project2.messenger.service.ChatService;
import com.project2.messenger.service.MessageService;
import com.project2.messenger.service.UserService;
import com.project2.messenger.serviceUtil.ServiceUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {
    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;

    public ChatController(ChatService chatService, MessageService messageService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/chats")
    public ResponseEntity<List<ChatView>> getAllChats() {
        User currentUser = ServiceUtil.getLoggedInUser();
        List<Chat> chats = chatService.findAllChats();
        List<ChatView> chatViews = new ArrayList<>();
        chats.forEach(chat -> chatViews.add(ChatView.from(chat, currentUser)));

        return ResponseEntity.ok(chatViews);
    }

    @PostMapping("/chats/private/{username}")
    public ResponseEntity<ChatView> openPrivateChat(@PathVariable String username) {
        User currentUser = ServiceUtil.getLoggedInUser();
        User toUser = userService.loadUserByUsername(username);
        Chat chat = chatService.openPrivateChat(toUser);

        return ResponseEntity.ok(ChatView.from(chat, currentUser));
    }

    @PostMapping("/chats/group")
    public ResponseEntity<ChatView> openGroupChat(@RequestParam List<String> userNames, @RequestParam(defaultValue = "Group chat") String name) {
        User currentUser = ServiceUtil.getLoggedInUser();
        List<User> users = userService.findAllUsers(userNames);
        Chat chat = chatService.openGroupChat(users, name);

        return ResponseEntity.ok(ChatView.from(chat, currentUser));
    }

    @GetMapping("/chats/{chatId}/messages")
    public ResponseEntity<ChatHistory> loadChatMessages(@PathVariable Long chatId, @RequestParam(required = false, defaultValue = Long.MAX_VALUE+"") Long beforeId) {
        return ResponseEntity.ok(messageService.loadChatMessages(chatId, beforeId));
    }
}
