package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.SavePostMediaDto;
import com.example.owoonwan.dto.dto.UpdatePostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "게시글 업데이트 응답 DTO")
public class UpdatePostResponse {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "게시글 ID", example = "1")
    private Long postId;

    @Schema(description = "미디어 파일 ID", example = "100")
    private Long mediaId;

    @Schema(description = "미디어 파일 URL", example = "http://example.com/media.jpg")
    private String url;

    public static UpdatePostResponse from(UpdatePostDto post) {
        return UpdatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .build();
    }

    public static UpdatePostResponse from(UpdatePostDto post, SavePostMediaDto media) {
        return UpdatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .mediaId(media.getMediaId())
                .url(media.getUrl())
                .build();
    }
}
