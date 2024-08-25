package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.SmsVerificationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class SmsVerification {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "SMS 인증 요청 DTO")
    public static class Request {

        @Schema(description = "인증 코드", example = "123456")
        private String verifyCode;
    }

    @Getter
    @Setter
    @Builder
    @Schema(description = "SMS 인증 응답 DTO")
    public static class Response {

        @Schema(description = "응답 설명", example = "인증 성공")
        private String description;

        @Schema(description = "사용자 ID", example = "user123")
        private String userId;

        public static Response from(SmsVerificationDto user, String description) {
            return Response.builder()
                    .userId(user.getUserId())
                    .description(description)
                    .build();
        }
    }
}
