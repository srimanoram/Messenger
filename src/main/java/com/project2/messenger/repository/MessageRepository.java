package com.project2.messenger.repository;

import com.project2.messenger.model.Chat;
import com.project2.messenger.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatAndIdLessThanOrderByIdDesc(Chat chat, Long beforeId, Pageable pageable);
}
