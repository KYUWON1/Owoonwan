package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.CreateCommentDto;
import lombok.*;

public class CreateComment {
    @Getter
    @Setter
    public static class Request{
        private String content;
        private Long postId;
        private String userId;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private String userId;
        private Long postId;

        public static Response from(CreateCommentDto from){
            return Response.builder()
                    .userId(from.getUserId())
                    .postId(from.getPostId())
                    .build();
        }
    }
}
