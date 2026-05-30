package com.project2.messenger.model;


import com.project2.messenger.model.records.ChatMemberId;
import com.project2.messenger.model.records.ChatRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_members",
        indexes = {
                @Index(name = "idx_chat_members_user", columnList = "user_id")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMember {

    @EmbeddedId
    private ChatMemberId id;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChatRole role;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

}