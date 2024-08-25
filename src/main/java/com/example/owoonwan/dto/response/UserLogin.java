package com.example.owoonwan.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserLogin {

    @Getter
    @Setter
    @AllArgsConstructor
    @Schema(description = "사용자 로그인 요청 DTO")
    public static class Request {

        @Schema(description = "사용자 ID", example = "john_doe123")
        private String userId;

        @Schema(description = "비밀번호", example = "securepassword")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @Schema(description = "사용자 로그인 응답 DTO")
    public static class Response {

        @Schema(description = "사용자 ID", example = "john_doe123")
        private String userId;

        @Schema(description = "인증 토큰", example = "Bearer...")
        private String token;

        @Schema(description = "응답 설명", example = "로그인이 성공적으로 완료되었습니다.")
        private String description;
    }
}
