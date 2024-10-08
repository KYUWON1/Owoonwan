package com.example.owoonwan.domain;

import com.example.owoonwan.type.GroupStatus;
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
@Table(name="workout_group")
@EntityListeners(AuditingEntityListener.class)
public class WorkOutGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @NotNull
    private String ownerId;

    @NotNull
    private Long maxMember;

    private Long memberCount = 0L;

    private String title;
    private String description;

    @CreatedDate
    private Date createdAt;
    private Date deletedAt;

    @LastModifiedDate
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private GroupStatus status;
}
