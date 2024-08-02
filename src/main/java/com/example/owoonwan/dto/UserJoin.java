package com.example.owoonwan.dto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

public class UserJoin {
    @Getter
    @Setter
    public static class Request {
        @NotNull
        @Min(10)
        private String userId;
        @NotNull
        @Min(10)
        private String password;
        @NotNull
        @Min(1)
        private String nickName;
        @NotNull
        private String phoneNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String userId;
        private String nickName;
        private String description;
        private LocalDateTime createdAt;

        public static Response from (UserJoinDto user,String description){
            return Response.builder()
                    .userId(user.getUserId())
                    .nickName(user.getNickName())
                    .description(description)
                    .createdAt(LocalDateTime.now())
                    .build();
        }

    }
}
