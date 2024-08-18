package com.example.owoonwan.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePostResponse {
    private String userId;
    private Long postId;
    private Long mediaId;
    private String url;
}
