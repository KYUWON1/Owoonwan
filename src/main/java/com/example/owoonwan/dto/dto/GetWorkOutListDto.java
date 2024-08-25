package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.WorkOutGroup;
import com.example.owoonwan.domain.WorkOutGroupMember;
import com.example.owoonwan.type.GroupStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "운동 그룹의 정보를 담고 있는 DTO")
public class GetWorkOutListDto {

    @Schema(description = "운동 그룹 ID", example = "101")
    private Long groupId;

    @Schema(description = "운동 그룹 소유자 ID", example = "user123")
    private String ownerId;

    @Schema(description = "운동 그룹의 최대 멤버 수", example = "10")
    private Long maxMember;

    @Schema(description = "현재 그룹의 멤버 수", example = "5")
    private Long memberCount = 0L;

    @Schema(description = "운동 그룹의 제목", example = "아침 러닝 그룹")
    private String title;

    @Schema(description = "운동 그룹에 대한 설명", example = "매일 아침 6시에 함께 러닝하는 그룹입니다.")
    private String description;

    @Schema(description = "운동 그룹이 생성된 날짜", example = "2024-08-25T10:00:00")
    private Date createdAt;

    @Schema(description = "운동 그룹의 상태 (활성화, 비활성화 등)", example = "ACTIVE")
    private GroupStatus status;

    @Schema(description = "운동 그룹 멤버들의 ID 리스트", example = "[\"user1\", \"user2\", \"user3\"]")
    private List<String> groupMemberIds;

    public static GetWorkOutListDto fromDomain(
            WorkOutGroup group,
            List<WorkOutGroupMember> members
    ){
        return GetWorkOutListDto.builder()
                .groupId(group.getGroupId())
                .ownerId(group.getOwnerId())
                .maxMember(group.getMaxMember())
                .memberCount(group.getMemberCount())
                .title(group.getTitle())
                .description(group.getDescription())
                .createdAt(group.getCreatedAt())
                .status(group.getStatus())
                .groupMemberIds(members.stream()
                        .map(WorkOutGroupMember::getUserId)
                        .collect(Collectors.toList()))
                .build();
    }
}
