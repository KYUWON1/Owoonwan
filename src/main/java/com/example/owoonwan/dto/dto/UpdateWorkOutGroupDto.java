package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.WorkOutGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateWorkOutGroupDto {
    private Long groupId;
    private String title;
    private String description;
    private Long upDatedMaxMember;

    public static UpdateWorkOutGroupDto fromDomain(WorkOutGroup group){
        return UpdateWorkOutGroupDto.builder()
                .title(group.getTitle())
                .groupId(group.getGroupId())
                .description(group.getDescription())
                .upDatedMaxMember(group.getMaxMember())
                .build();
    }
}
