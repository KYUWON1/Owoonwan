package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostComment;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostCommentDto {
    private String userId;
    private String updateContent;
    private Date updatedAt;

    public static UpdatePostCommentDto fromDomain(PostComment comment){
        return UpdatePostCommentDto.builder()
                .userId(comment.getUserId())
                .updateContent(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
