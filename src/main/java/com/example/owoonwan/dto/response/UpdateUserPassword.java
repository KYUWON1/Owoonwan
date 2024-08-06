package com.example.owoonwan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class UpdateUserPassword {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request{
        private String passwordBefore;
        private String passwordAfter;
        private String passwordDoubleCheck;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response{
        private String userId;
    }


}
