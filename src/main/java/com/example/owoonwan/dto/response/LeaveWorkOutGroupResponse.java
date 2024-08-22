package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.LeaveWorkOutGroupDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveWorkOutGroupResponse {
    private Long groupId;
    private String userId;

    public static LeaveWorkOutGroupResponse from(LeaveWorkOutGroupDto dto){
        return LeaveWorkOutGroupResponse.builder()
                .groupId(dto.getGroupId())
                .userId(dto.getUserId())
                .build();
    }
}
