package com.example.owoonwan.dto.response;

import com.example.owoonwan.domain.PostLike;
import com.example.owoonwan.dto.dto.CreateLikeDto;
import com.example.owoonwan.type.LikeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateLikeAndCancelResponse {
    private String userId;
    private Date createdAt;
    private LikeType type;

    public static CreateLikeAndCancelResponse from(CreateLikeDto dto){
        CreateLikeAndCancelResponse response = new CreateLikeAndCancelResponse();
        response.setUserId(dto.getUserId());
        response.setCreatedAt(dto.getCreatedAt());
        if(dto.isLike()){
            response.setType(LikeType.LIKE);
        }else{
            response.setType(LikeType.CANCEL_LIKE);
        }
        return response;
    }
}
