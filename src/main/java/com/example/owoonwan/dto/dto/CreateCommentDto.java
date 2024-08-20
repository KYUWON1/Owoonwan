package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostComment;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentDto {
    private String userId;
    private Long postId;

    public static CreateCommentDto fromDomain(PostComment save){
        return CreateCommentDto.builder()
                .userId(save.getUserId())
                .postId(save.getPostId())
                .build();
    }
}
