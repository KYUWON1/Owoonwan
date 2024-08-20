package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.SmsVerificationDto;
import lombok.*;

public class SmsVerification {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String verifyCode;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String description;
        private String userId;

        public static Response from(SmsVerificationDto user, String description){
            return Response.builder()
                    .userId(user.getUserId())
                    .description(description)
                    .build();
        }
    }
}
