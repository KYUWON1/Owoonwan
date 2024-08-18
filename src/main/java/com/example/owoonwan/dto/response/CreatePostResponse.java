package com.example.owoonwan.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostResponse {
    private String userId;
    private Long postId;
    private Long mediaId;
    private String url;
}
