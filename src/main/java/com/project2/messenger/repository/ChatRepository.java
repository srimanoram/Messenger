package com.project2.messenger.repository;

import com.project2.messenger.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {


}
