package com.example.owoonwan.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class UpdateUserIdAndNickName {

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    @Schema(description = "사용자 ID 및 닉네임 업데이트 요청 DTO")
    public static class Request {

        @Schema(description = "사용자 ID", example = "user123")
        private String userId;

        @Schema(description = "닉네임", example = "NewNickName")
        private String nickName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "사용자 ID 및 닉네임 업데이트 응답 DTO")
    public static class Response {

        @Schema(description = "사용자 ID", example = "user123")
        private String userId;

        @Schema(description = "닉네임", example = "NewNickName")
        private String nickName;
    }
}
