package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "게시물 미디어 저장 DTO")
public class SavePostMediaDto {

    @Schema(description = "미디어 ID", example = "202")
    private Long mediaId;

    @Schema(description = "미디어 URL", example = "https://example.com/media.jpg")
    private String url;
}
