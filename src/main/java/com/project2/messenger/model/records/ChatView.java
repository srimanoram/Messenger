package com.project2.messenger.model.records;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.User;

import java.util.ArrayList;
import java.util.List;

public record ChatView(Long chatId, String displayName, Boolean isPrivate) {
    /*public static List<ChatView> getChatView(List<Chat> chats, User currentUser) {
        List<ChatView> chatViews = new ArrayList<>();
        chats.forEach(chat -> {
            if (chat.getIsPrivate()) {
                String displayName = (currentUser.getUsername().equals(chat.getUser1().getUsername())) ? chat.getUser2().getUsername() : chat.getUser1().getUsername();
                chatViews.add(new ChatView(chat, displayName));
            }
        });
        return chatViews;
    }*/

    public static ChatView from(Chat chat, User currentUser) {
        if (chat.getIsPrivate()) {
            String displayName = (currentUser.getUsername().equals(chat.getUser1().getUsername())) ? chat.getUser2().getUsername() : chat.getUser1().getUsername();
            return new ChatView(chat.getId(), displayName, true);
        } else {
            return new ChatView(chat.getId(), chat.getName(), false);
        }
    }
}
