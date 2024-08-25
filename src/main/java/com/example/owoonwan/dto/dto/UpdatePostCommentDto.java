package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "댓글 수정 DTO")
public class UpdatePostCommentDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "수정된 댓글 내용", example = "이것은 수정된 댓글입니다.")
    private String updateContent;

    @Schema(description = "댓글 수정 시간", example = "2024-08-25T10:15:30")
    private Date updatedAt;

    public static UpdatePostCommentDto fromDomain(PostComment comment) {
        return UpdatePostCommentDto.builder()
                .userId(comment.getUserId())
                .updateContent(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
