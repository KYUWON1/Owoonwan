package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.LeaveWorkOutGroupDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "운동 그룹 탈퇴 응답 DTO")
public class LeaveWorkOutGroupResponse {

    @Schema(description = "운동 그룹 ID")
    private Long groupId;

    @Schema(description = "사용자 ID")
    private String userId;

    public static LeaveWorkOutGroupResponse from(LeaveWorkOutGroupDto dto) {
        return LeaveWorkOutGroupResponse.builder()
                .groupId(dto.getGroupId())
                .userId(dto.getUserId())
                .build();
    }
}
