package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "컨텐츠 정보 DTO")
public class ContentDto {

    @Schema(description = "내용", example = "예시 내용입니다.")
    private String content;
}
