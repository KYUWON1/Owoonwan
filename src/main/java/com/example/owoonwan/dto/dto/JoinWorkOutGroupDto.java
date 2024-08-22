package com.example.owoonwan.dto.dto;

import com.example.owoonwan.type.GroupStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinWorkOutGroupDto {
    private Long groupId;
    private String userId;
    private String title;
}
