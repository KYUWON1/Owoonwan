package com.example.owoonwan.domain;

import com.example.owoonwan.type.MediaType;
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
@Table(name = "post_media")
@EntityListeners(AuditingEntityListener.class)
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    // ERD PK값 수정. postID는 여러개 가질수있음.
    @NotNull
    private Long postId;

    @NotNull
    private String url;

    @NotNull()
    @Enumerated(EnumType.STRING)
    private MediaType type;

    // order 예약어 사용불가
    /*
        1. spring에서 마지막 번호를 가지고 +1 해서 저장(동시성문제 가능)
        2. database에서 트리거 사용
        3. 다른 해결책 찾아보기
     */
    @NotNull
    private Integer sequence;

    @CreatedDate
    private Date createdAt;
    private Date deletedAt;

    @LastModifiedDate
    private Date updatedAt;
}
