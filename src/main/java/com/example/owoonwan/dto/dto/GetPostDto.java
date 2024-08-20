package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.Post;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본생성자 오류, 문제해결
public class GetPostDto {
    private String userId;
    private Long postId;

    private String content;
    private Date updatedAt;

    public static GetPostDto FromEntity(Post post){
        return GetPostDto.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
