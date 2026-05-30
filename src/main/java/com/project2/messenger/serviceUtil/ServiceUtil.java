package com.project2.messenger.serviceUtil;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.ChatMember;
import com.project2.messenger.model.records.ChatRole;
import com.project2.messenger.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ServiceUtil {

    private ServiceUtil() {}
    public static User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user;
        if (auth == null || (user = (User) auth.getPrincipal())==null) {
            throw new IllegalStateException("User not found");
        }
        return user;
    }

    public static List<ChatMember> getChatMemberList(Chat chat, Set<User> users) {
        List<ChatMember> chatMembers = new ArrayList<>();
        users.forEach(user1 -> {
            ChatMember chatMember = new ChatMember();
            chatMember.setChat(chat);
            chatMember.setUser(user1);
            chatMember.setRole(ChatRole.MEMBER);
            chatMembers.add(chatMember);
        });
        return chatMembers;
    }
}
