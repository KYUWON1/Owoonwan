package com.example.owoonwan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

public class UserLogin {
   @Getter
   @Setter
   @AllArgsConstructor
    public static class Request{
        private String userId;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class Response{
        private String userId;
        private String token;
        private String description;
    }
}
