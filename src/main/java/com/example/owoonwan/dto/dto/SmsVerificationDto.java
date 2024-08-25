package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "SMS 인증 DTO")
public class SmsVerificationDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;
}
