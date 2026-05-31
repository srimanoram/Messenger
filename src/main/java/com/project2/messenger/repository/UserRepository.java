package com.project2.messenger.repository;

import com.project2.messenger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
    List<User> findAllByUsername(List<String> userNames);
}