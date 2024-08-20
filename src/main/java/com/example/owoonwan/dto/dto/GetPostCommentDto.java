package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostComment;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPostCommentDto {
    private String userId;
    private String content;
    private Date createdAt;

    public static GetPostCommentDto fromDomain(PostComment post){
        return GetPostCommentDto.builder()
                .userId(post.getUserId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
