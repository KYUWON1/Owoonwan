package com.example.owoonwan.dto.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveWorkOutGroupDto {
    private Long groupId;
    private String userId;
}
