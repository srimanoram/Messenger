package com.project2.messenger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "chats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user1", "user2"}, name = "unique_user_pair")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="chat_name")
    private String name;

    @Column(name="is_private",  nullable = false)
    private Boolean isPrivate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2")
    private User user2;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
