package com.example.owoonwan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UpdateUserIdAndNickName {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request{
        private String userId;
        private String nickName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String userId;
    }
}
