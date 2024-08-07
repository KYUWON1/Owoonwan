package com.example.owoonwan.domain;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name="posts")
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotNull
    @Column(unique = true)
    private String userId;

    private String content;

    @CreatedDate
    private Date createdAt;
    private Date deletedAt;

    @LastModifiedDate
    private Date updatedAt;
}
