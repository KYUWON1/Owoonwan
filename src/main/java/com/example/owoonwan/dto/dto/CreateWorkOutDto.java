package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.WorkOutGroup;
import com.example.owoonwan.type.GroupStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@Schema(description = "운동 그룹 생성 DTO")
public class CreateWorkOutDto {

    @Schema(description = "그룹 소유자 ID", example = "user123")
    private String ownerId;

    @Schema(description = "최대 멤버 수", example = "20")
    private Long maxMember;

    @Schema(description = "그룹 제목", example = "아침 운동 그룹")
    private String title;

    @Schema(description = "생성일", example = "2023-08-01T10:00:00")
    private Date createdAt;

    @Schema(description = "그룹 상태", example = "ACTIVE")
    private GroupStatus status;

    public static CreateWorkOutDto fromDomain(WorkOutGroup group) {
        return CreateWorkOutDto.builder()
                .ownerId(group.getOwnerId())
                .title(group.getTitle())
                .maxMember(group.getMaxMember())
                .createdAt(group.getCreatedAt())
                .status(group.getStatus())
                .build();
    }
}
