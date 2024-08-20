package com.example.owoonwan.dto.response;

import com.example.owoonwan.domain.PostComment;
import com.example.owoonwan.dto.dto.GetPostCommentDto;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCommentResponse {
    private String userId;
    private Long commentId;
    private String content;
    private Date createdAt;

    public static GetCommentResponse from(GetPostCommentDto post){
        return GetCommentResponse.builder()
                .userId(post.getUserId())
                .commentId(post.getCommentId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
