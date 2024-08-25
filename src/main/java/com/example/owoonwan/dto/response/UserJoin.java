package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.UserJoinDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UserJoin {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(name="UserJoinRequest",description = "사용자 가입 요청 DTO")
    public static class Request {

        @NotNull
        @Min(10)
        @Schema(name="UserJoinResponse",description = "사용자 ID", example =
                "john_doe123")
        private String userId;

        @NotNull
        @Min(10)
        @Schema(description = "비밀번호", example = "securepassword")
        private String password;

        @NotNull
        @Min(1)
        @Schema(description = "닉네임", example = "JohnDoe")
        private String nickName;

        @NotNull
        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phoneNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "사용자 가입 응답 DTO")
    public static class Response {

        @Schema(description = "사용자 ID", example = "john_doe123")
        private String userId;

        @Schema(description = "닉네임", example = "JohnDoe")
        private String nickName;

        @Schema(description = "응답 설명", example = "사용자 가입이 완료되었습니다.")
        private String description;

        @Schema(description = "생성 일시", example = "2023-08-25T14:30:00")
        private LocalDateTime createdAt;

        public static Response from(UserJoinDto user, String description) {
            return Response.builder()
                    .userId(user.getUserId())
                    .nickName(user.getNickName())
                    .description(description)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }
}
