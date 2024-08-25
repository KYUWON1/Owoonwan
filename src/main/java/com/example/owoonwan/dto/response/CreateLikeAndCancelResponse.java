package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.CreateLikeDto;
import com.example.owoonwan.type.LikeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Schema(description = "좋아요 및 취소 응답 DTO")
public class CreateLikeAndCancelResponse {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "생성 날짜", example = "2023-08-25T10:00:00Z")
    private Date createdAt;

    @Schema(description = "좋아요 타입 (LIKE, CANCEL_LIKE)", example = "LIKE")
    private LikeType type;

    public static CreateLikeAndCancelResponse from(CreateLikeDto dto) {
        CreateLikeAndCancelResponse response = new CreateLikeAndCancelResponse();
        response.setUserId(dto.getUserId());
        response.setCreatedAt(dto.getCreatedAt());
        response.setType(dto.isLike() ? LikeType.LIKE : LikeType.CANCEL_LIKE);
        return response;
    }
}
