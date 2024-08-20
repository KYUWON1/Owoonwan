package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostLike;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class GetPostLikeInfoDto {
    private String userId;
    private Date createdAt;

    public static GetPostLikeInfoDto fromDomain(PostLike like){
        return GetPostLikeInfoDto.builder()
                .userId(like.getUserId())
                .createdAt(like.getCreatedAt())
                .build();
    }
}
