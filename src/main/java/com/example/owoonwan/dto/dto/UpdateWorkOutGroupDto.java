package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.WorkOutGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "운동 그룹 업데이트 DTO")
public class UpdateWorkOutGroupDto {

    @Schema(description = "그룹 ID", example = "1001")
    private Long groupId;

    @Schema(description = "그룹 제목", example = "아침 조깅 모임")
    private String title;

    @Schema(description = "그룹 설명", example = "아침마다 조깅하는 모임입니다.")
    private String description;

    @Schema(description = "업데이트된 최대 멤버 수", example = "20")
    private Long upDatedMaxMember;

    public static UpdateWorkOutGroupDto fromDomain(WorkOutGroup group) {
        return UpdateWorkOutGroupDto.builder()
                .title(group.getTitle())
                .groupId(group.getGroupId())
                .description(group.getDescription())
                .upDatedMaxMember(group.getMaxMember())
                .build();
    }
}
