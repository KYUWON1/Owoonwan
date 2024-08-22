package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.WorkOutGroup;
import com.example.owoonwan.domain.WorkOutGroupMember;
import com.example.owoonwan.type.GroupStatus;
import lombok.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetWorkOutListDto {
    private Long groupId;
    private String ownerId;
    private Long maxMember;
    private Long memberCount = 0L;
    private String title;
    private String description;
    private Date createdAt;
    private GroupStatus status;
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
