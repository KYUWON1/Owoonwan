package com.example.owoonwan.dto.response;

import lombok.*;

public class UpdateUserIdAndNickName {

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Request{
        private String userId;
        private String nickName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String userId;
        private String nickName;
    }
}
