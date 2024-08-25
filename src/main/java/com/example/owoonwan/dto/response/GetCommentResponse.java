package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.GetPostCommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "댓글 정보 응답 DTO")
public class GetCommentResponse {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;

    @Schema(description = "댓글 내용", example = "This is a comment.")
    private String content;

    @Schema(description = "댓글 생성 날짜", example = "2023-08-25T10:15:30")
    private Date createdAt;

    public static GetCommentResponse from(GetPostCommentDto post) {
        return GetCommentResponse.builder()
                .userId(post.getUserId())
                .commentId(post.getCommentId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
