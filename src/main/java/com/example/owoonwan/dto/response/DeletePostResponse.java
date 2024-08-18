package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.DeletePostDto;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletePostResponse {
    private Long postId;
    private String userId;
    private Date deletedAt;

    public static DeletePostResponse from(DeletePostDto data,String userId){
        return DeletePostResponse.builder()
                .deletedAt(data.getDeletedAt())
                .postId(data.getPostId())
                .userId(userId)
                .build();
    }
}
