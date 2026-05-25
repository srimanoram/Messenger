package com.project2.messenger.repository;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUser1AndUser2AndIsPrivateTrue(User user1, User user2);

    @Query("select c from Chat c join ChatMember m on m.chat = c where m.user = :user")
    List<Chat> findChatsForUser(@Param("user") User user);
}
