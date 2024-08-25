package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

public class UpdatePostComment {

    @Getter
    @Setter
    @Schema(description = "댓글 수정 요청 DTO")
    public static class Request {

        @Schema(description = "댓글 ID", example = "1")
        private Long commentId;

        @Schema(description = "수정된 댓글 내용", example = "이것은 수정된 댓글입니다.")
        private String content;
    }

    @Builder
    @Getter
    @Setter
    @Schema(description = "댓글 수정 응답 DTO")
    public static class Response {

        @Schema(description = "사용자 ID", example = "user123")
        private String userId;

        @Schema(description = "수정된 댓글 내용", example = "이것은 수정된 댓글입니다.")
        private String updateContent;

        @Schema(description = "댓글 수정 시간", example = "2024-08-25T10:15:30")
        private Date updatedAt;

        public static Response from(UpdatePostCommentDto dto) {
            return Response.builder()
                    .userId(dto.getUserId())
                    .updateContent(dto.getUpdateContent())
                    .updatedAt(dto.getUpdatedAt())
                    .build();
        }
    }
}
