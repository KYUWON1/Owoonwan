package com.example.owoonwan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="workout_group_member")
@EntityListeners(AuditingEntityListener.class)
public class WorkOutGroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @NotNull
    private String userId;

    @NotNull
    private Long groupId;

    @CreatedDate
    private Date createdAt;
}
