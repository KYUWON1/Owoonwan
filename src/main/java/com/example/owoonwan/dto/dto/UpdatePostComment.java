package com.example.owoonwan.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class UpdatePostComment {
    @Getter
    @Setter
    public static class Request{
        private Long commentId;
        private String content;
    }

    @Builder
    @Getter
    @Setter
    public static class Response {
        private String userId;
        private String updateContent;
        private Date updatedAt;

        public static Response from(UpdatePostCommentDto dto){
            return Response.builder()
                    .userId(dto.getUserId())
                    .updateContent(dto.getUpdateContent())
                    .updatedAt(dto.getUpdatedAt())
                    .build();
        }
    }
}
