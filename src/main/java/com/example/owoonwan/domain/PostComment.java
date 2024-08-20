package com.example.owoonwan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "post_comment")
@EntityListeners(AuditingEntityListener.class)
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @NotNull
    private Long postId;

    @NotNull
    private String userId;

    @NotNull
    private String content;

    @CreatedDate
    private Date createdAt;
    private Date deletedAt;

    @LastModifiedDate
    private Date updatedAt;
}
