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
@Table(name = "post_like")
@EntityListeners(AuditingEntityListener.class)
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @NotNull
    private Long postId;

    @NotNull
    private String userId;

    @CreatedDate
    private Date createdAt;
    private Date deletedAt;
}
