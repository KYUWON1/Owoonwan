package com.example.owoonwan.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostResponse {
    String userId;
    Long postId;
    Long mediaId;
    String url;

}
