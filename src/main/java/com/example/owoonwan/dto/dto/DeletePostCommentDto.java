package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "댓글 삭제 요청 DTO")
public class DeletePostCommentDto {

    @Schema(description = "삭제할 댓글 ID", example = "123")
    private Long commentId;
}
