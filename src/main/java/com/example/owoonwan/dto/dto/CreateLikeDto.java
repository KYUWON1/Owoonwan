package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostLike;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "좋아요 생성 DTO")
public class CreateLikeDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "좋아요 생성 날짜", example = "2024-08-25T10:00:00Z")
    private Date createdAt;

    @Schema(description = "좋아요 상태 (true: 좋아요, false: 좋아요 취소)", example = "true")
    private boolean like;

    public static CreateLikeDto fromDomainDoLike(PostLike like) {
        return new CreateLikeDto(like.getUserId(), like.getCreatedAt(), true);
    }

    public static CreateLikeDto fromDomainUnDoLike(PostLike like) {
        return new CreateLikeDto(like.getUserId(), like.getCreatedAt(), false);
    }
}
