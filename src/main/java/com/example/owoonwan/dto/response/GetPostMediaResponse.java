package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.GetPostDto;
import com.example.owoonwan.dto.dto.GetPostMediaDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "게시글 및 미디어 정보 응답 DTO")
public class GetPostMediaResponse {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "게시글 ID", example = "1")
    private Long postId;

    @Schema(description = "게시글 내용", example = "This is a post content.")
    private String content;

    @Schema(description = "게시글 수정 날짜", example = "2023-08-25T10:15:30")
    private Date updatedAt;

    @Schema(description = "미디어 정보 리스트")
    private List<GetPostMediaDto.MediaInfo> mediaInfos;

    public static GetPostMediaResponse fromNoMedia(GetPostDto post) {
        return GetPostMediaResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static GetPostMediaResponse fromExistMedia(GetPostDto post, List<GetPostMediaDto.MediaInfo> infoList) {
        return GetPostMediaResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .mediaInfos(infoList)
                .build();
    }
}
