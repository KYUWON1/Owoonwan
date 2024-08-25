package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.JoinWorkOutGroupDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "운동 그룹 참여 응답 DTO")
public class JoinWorkOutResponse {

    @Schema(description = "그룹 ID", example = "100")
    private Long groupId;

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "그룹 제목", example = "Morning Jogging Group")
    private String title;

    public static JoinWorkOutResponse from(JoinWorkOutGroupDto dto) {
        return JoinWorkOutResponse.builder()
                .groupId(dto.getGroupId())
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .build();
    }
}
