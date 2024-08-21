package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.WorkOutGroup;
import com.example.owoonwan.type.GroupStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class CreateWorkOutDto {
    private String ownerId;
    private Long maxMember;
    private String title;
    private Date createdAt;
    private GroupStatus status;

    public static CreateWorkOutDto fromDomain(WorkOutGroup group){
        return CreateWorkOutDto.builder()
                .ownerId(group.getOwnerId())
                .title(group.getTitle())
                .maxMember(group.getMaxMember())
                .createdAt(group.getCreatedAt())
                .status(group.getStatus())
                .build();
    }
}
