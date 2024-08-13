package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.GetPostDto;
import com.example.owoonwan.dto.dto.GetPostMediaDto;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPostMediaResponse {
    private String userId;
    private Long postId;
    private String content;
    private Date updatedAt;
    private List<GetPostMediaDto.MediaInfo> mediaInfos;

    public static GetPostMediaResponse fromNoMedia(GetPostDto post){
        return GetPostMediaResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static GetPostMediaResponse fromExistMedia(GetPostDto post,
                                                      List<GetPostMediaDto.MediaInfo> infoList){
        return GetPostMediaResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .mediaInfos(infoList)
                .build();
    }
}
