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
    @Schema(name="UserLoginRequest",description = "사용자 로그인 요청 DTO")
    public static class Request {

        @Schema(description = "사용자 ID", example = "testId")
        private String userId;

        @Schema(description = "비밀번호", example = "test1234")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @Schema(name="UserLoginResponse",description = "사용자 로그인 응답 DTO")
    public static class Response {

        @Schema(description = "로그인", example = "true")
        private String success;

        @Schema(description = "메세지", example = "Authentication successful")
        private String token;
    }
}
