package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.CreateCommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class CreateComment {

    @Getter
    @Setter
    @Schema(name="CreateCommentRequest",description = "댓글 생성 요청 DTO")
    public static class Request {
        @Schema(description = "댓글 내용", example = "This is a comment.")
        private String content;

        @Schema(description = "게시글 ID", example = "1")
        private Long postId;

        @Schema(description = "사용자 ID", example = "user123")
        private String userId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(name="CreateCommentResponse",description = "댓글 생성 응답 DTO")
    public static class Response {
        @Schema(description = "사용자 ID", example = "user123")
        private String userId;

        @Schema(description = "게시글 ID", example = "1")
        private Long postId;

        public static Response from(CreateCommentDto from) {
            return Response.builder()
                    .userId(from.getUserId())
                    .postId(from.getPostId())
                    .build();
        }
    }
}
