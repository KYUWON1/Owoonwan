package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.CreatePostDto;
import com.example.owoonwan.dto.dto.SavePostMediaDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "게시글 생성 응답 DTO")
public class CreatePostResponse {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "게시글 ID", example = "1")
    private Long postId;

    @Schema(description = "미디어 ID", example = "100")
    private Long mediaId;

    @Schema(description = "미디어 URL", example = "http://example.com/media/100")
    private String url;

    public static CreatePostResponse from(CreatePostDto post) {
        return CreatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .build();
    }

    public static CreatePostResponse from(CreatePostDto post, SavePostMediaDto media) {
        return CreatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .mediaId(media.getMediaId())
                .url(media.getUrl())
                .build();
    }
}
