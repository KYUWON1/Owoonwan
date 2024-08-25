package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "게시글 생성 DTO")
public class CreatePostDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "게시글 ID", example = "1")
    private Long postId;
}
