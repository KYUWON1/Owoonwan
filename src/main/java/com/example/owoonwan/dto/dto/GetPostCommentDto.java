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
@Schema(description = "게시글 댓글 조회 DTO")
public class GetPostCommentDto {

    @Schema(description = "댓글 작성자 ID", example = "user123")
    private String userId;

    @Schema(description = "댓글 ID", example = "456")
    private Long commentId;

    @Schema(description = "댓글 내용", example = "This is a comment.")
    private String content;

    @Schema(description = "댓글 작성 일자", example = "2024-08-25T10:15:30")
    private Date createdAt;

    public static GetPostCommentDto fromDomain(PostComment post) {
        return GetPostCommentDto.builder()
                .userId(post.getUserId())
                .commentId(post.getCommentId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
