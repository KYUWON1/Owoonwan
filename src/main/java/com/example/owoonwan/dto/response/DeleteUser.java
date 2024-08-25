package com.example.owoonwan.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "사용자 삭제 응답 DTO")
public class DeleteUser {

    @Schema(description = "삭제된 사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "사용자가 삭제된 날짜", example = "2024-08-25")
    private LocalDate deletedAt;
}
