package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "운동 그룹 탈퇴 DTO")
public class LeaveWorkOutGroupDto {

    @Schema(description = "운동 그룹 ID", example = "101")
    private Long groupId;

    @Schema(description = "탈퇴하는 사용자의 ID", example = "user123")
    private String userId;
}
