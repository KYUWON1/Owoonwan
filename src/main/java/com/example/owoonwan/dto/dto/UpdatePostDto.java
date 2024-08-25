package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "게시글 수정 DTO")
public class UpdatePostDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "게시글 ID", example = "101")
    private Long postId;
}
