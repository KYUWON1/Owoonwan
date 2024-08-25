package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.DeletePostCommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "댓글 삭제 응답 DTO")
public class DeletePostCommentResponse {

    @Schema(description = "삭제된 댓글 ID", example = "12345")
    private Long commentId;

    public static DeletePostCommentResponse from(DeletePostCommentDto dto) {
        return new DeletePostCommentResponse(dto.getCommentId());
    }
}
