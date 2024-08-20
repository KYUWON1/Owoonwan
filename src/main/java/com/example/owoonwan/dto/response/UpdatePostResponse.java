package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.SavePostMediaDto;
import com.example.owoonwan.dto.dto.UpdatePostDto;
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

    public static UpdatePostResponse from(UpdatePostDto post){
        return UpdatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .build();
    }

    public static UpdatePostResponse from(UpdatePostDto post,
                                          SavePostMediaDto media){
        return UpdatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .mediaId(media.getMediaId())
                .url(media.getUrl())
                .build();
    }
}
