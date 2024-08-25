package com.example.owoonwan.dto.dto;

import com.example.owoonwan.type.GroupStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "운동 그룹에 가입하기 위한 DTO")
public class JoinWorkOutGroupDto {

    @Schema(description = "운동 그룹 ID", example = "101")
    private Long groupId;

    @Schema(description = "가입하는 사용자의 ID", example = "user123")
    private String userId;

    @Schema(description = "운동 그룹의 제목", example = "아침 러닝 그룹")
    private String title;
}
