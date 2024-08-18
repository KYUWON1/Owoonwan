package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.CreatePostDto;
import com.example.owoonwan.dto.dto.SavePostMediaDto;
import com.example.owoonwan.dto.dto.UpdatePostDto;
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

    public static CreatePostResponse from(CreatePostDto post){
        return CreatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .build();
    }

    public static CreatePostResponse from(CreatePostDto post,
                                          SavePostMediaDto media){
        return CreatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .mediaId(media.getMediaId())
                .url(media.getUrl())
                .build();
    }
}
