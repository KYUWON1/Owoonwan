package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostLike;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@Schema(description = "게시글 좋아요 정보 DTO")
public class GetPostLikeInfoDto {

    @Schema(description = "좋아요를 누른 사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "좋아요를 누른 일자", example = "2024-08-25T10:15:30")
    private Date createdAt;

    public static GetPostLikeInfoDto fromDomain(PostLike like) {
        return GetPostLikeInfoDto.builder()
                .userId(like.getUserId())
                .createdAt(like.getCreatedAt())
                .build();
    }
}
