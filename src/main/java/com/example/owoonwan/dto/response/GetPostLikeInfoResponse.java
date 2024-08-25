package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.GetPostLikeInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@Schema(description = "게시글 좋아요 정보 응답 DTO")
public class GetPostLikeInfoResponse {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "좋아요 생성 날짜", example = "2023-08-25T10:15:30")
    private Date createdAt;

    public static GetPostLikeInfoResponse from(GetPostLikeInfoDto like) {
        return GetPostLikeInfoResponse.builder()
                .userId(like.getUserId())
                .createdAt(like.getCreatedAt())
                .build();
    }
}
