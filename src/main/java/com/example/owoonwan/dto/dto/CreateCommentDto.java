package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 생성 DTO")
public class CreateCommentDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "게시글 ID", example = "1")
    private Long postId;

    public static CreateCommentDto fromDomain(PostComment save) {
        return CreateCommentDto.builder()
                .userId(save.getUserId())
                .postId(save.getPostId())
                .build();
    }
}
