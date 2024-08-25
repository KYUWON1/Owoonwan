package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@Schema(description = "게시글 미디어 삭제 DTO")
public class deletePostMediaDto {

    @Schema(description = "삭제할 게시글 ID", example = "123")
    private Long postId;

    @Schema(description = "미디어 삭제 일시", example = "2024-08-25T14:30:00.000Z")
    private Date deletedAt;
}
