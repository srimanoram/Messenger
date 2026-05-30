package com.project2.messenger.repository;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.ChatMember;
import com.project2.messenger.model.records.ChatMemberId;
import com.project2.messenger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, ChatMemberId> {
    List<ChatMember> findByChat(Chat chat);
    boolean existsByChatAndUser(Chat chat, User user);
}
