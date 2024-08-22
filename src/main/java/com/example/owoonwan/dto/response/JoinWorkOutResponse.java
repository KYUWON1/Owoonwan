package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.JoinWorkOutGroupDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinWorkOutResponse {
    private Long groupId;
    private String userId;
    private String title;

    public static JoinWorkOutResponse from(JoinWorkOutGroupDto dto){
        return JoinWorkOutResponse.builder()
                .groupId(dto.getGroupId())
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .build();
    }
}
