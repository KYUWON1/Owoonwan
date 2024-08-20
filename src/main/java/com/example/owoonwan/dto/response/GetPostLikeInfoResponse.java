package com.example.owoonwan.dto.response;

import com.example.owoonwan.domain.PostLike;
import com.example.owoonwan.dto.dto.GetPostLikeInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class GetPostLikeInfoResponse {
    private String userId;
    private Date createdAt;

    public static GetPostLikeInfoResponse from(GetPostLikeInfoDto like){
        return GetPostLikeInfoResponse.builder()
                .userId(like.getUserId())
                .createdAt(like.getCreatedAt())
                .build();
    }
}
