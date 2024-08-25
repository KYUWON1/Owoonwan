package com.example.owoonwan.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UpdateUserPassword {

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    @Schema(description = "사용자 비밀번호 업데이트 요청 DTO")
    public static class Request {

        @Schema(description = "기존 비밀번호", example = "oldPassword123")
        private String passwordBefore;

        @Schema(description = "새 비밀번호", example = "newPassword123")
        private String passwordAfter;

        @Schema(description = "새 비밀번호 확인", example = "newPassword123")
        private String passwordDoubleCheck;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    @Schema(description = "사용자 비밀번호 업데이트 응답 DTO")
    public static class Response {

        @Schema(description = "사용자 ID", example = "user123")
        private String userId;
    }
}
