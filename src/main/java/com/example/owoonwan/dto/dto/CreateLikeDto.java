package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class CreateLikeDto {
    private String userId;
    private Date createdAt;
    private boolean like;

    public static CreateLikeDto fromDomainDoLike(PostLike like){
        return new CreateLikeDto(like.getUserId(),like.getCreatedAt(),true);
    }
    public static CreateLikeDto fromDomainUnDoLike(PostLike like){
        return new CreateLikeDto(like.getUserId(),like.getCreatedAt(),false);
    }
}
