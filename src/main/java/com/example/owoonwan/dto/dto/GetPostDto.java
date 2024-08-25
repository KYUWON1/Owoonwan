package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "게시글 조회 DTO")
public class GetPostDto {

    @Schema(description = "게시글 작성자 ID", example = "user123")
    private String userId;

    @Schema(description = "게시글 ID", example = "789")
    private Long postId;

    @Schema(description = "게시글 내용", example = "This is a post content.")
    private String content;

    @Schema(description = "게시글 수정 일자", example = "2024-08-25T10:15:30")
    private Date updatedAt;

    public static GetPostDto FromEntity(Post post) {
        return GetPostDto.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
