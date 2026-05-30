package com.project2.messenger.service;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.ChatMember;
import com.project2.messenger.model.records.ChatRole;
import com.project2.messenger.model.User;
import com.project2.messenger.repository.ChatMemberRepository;
import com.project2.messenger.repository.ChatRepository;
import com.project2.messenger.serviceUtil.ServiceUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
//    private final UserService userService;

    public ChatService(ChatRepository chatRepository, ChatMemberRepository chatMemberRepository/*, UserService userService*/) {
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
//        this.userService = userService;
    }

    public List<Chat> findAllChats() {
        User user = ServiceUtil.getLoggedInUser();
        return chatRepository.findChatsForUser(user);
    }

    @Transactional
    public Chat openPersonalChat(User user2) {
        User user = ServiceUtil.getLoggedInUser();

        User smaller, larger;
        if (user.getId() < user2.getId()) {
            smaller = user;
            larger = user2;
        } else {
            smaller = user2;
            larger = user;
        }

        Optional<Chat> chatOptional = chatRepository.findByUser1AndUser2AndIsPrivateTrue(smaller, larger);
        Chat chat = chatOptional.orElseGet(() -> {
            Chat newChat = new Chat();
            newChat.setUser1(smaller);
            newChat.setUser2(larger);
            newChat.setIsPrivate(true);
            chatRepository.save(newChat);

            List<ChatMember> chatMembers = ServiceUtil.getChatMemberList(newChat, Set.of(user, user2));
            chatMemberRepository.saveAll(chatMembers);

            return newChat;
        });



        return chat;
    }

    @Transactional
    public Chat createGroupChat(List<User> users, String name) {
        User user = ServiceUtil.getLoggedInUser();
        Set<User> usersSet = new LinkedHashSet<>(users);
        usersSet.remove(user);
        usersSet.add(user);

        Chat chat = new Chat();
        chat.setName(name);
        chat.setIsPrivate(false);

        chatRepository.save(chat);

        List<ChatMember> chatMembers = ServiceUtil.getChatMemberList(chat, usersSet);
        chatMembers.getLast().setRole(ChatRole.ADMIN);

        chatMemberRepository.saveAll(chatMembers);
        return chat;

    }

   /* public Chat openGroupChat(Chat chat) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user;
        if (auth == null || (user = (User) auth.getPrincipal())==null) {
            throw new IllegalStateException("User not found");
        }

        chatMemberRepository

    }*/
}
