package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.UpdateWorkOutGroupDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UpdateWorkOutGroup {

    @Getter
    @Setter
    @Schema(name="UpdateWorkOutGroupRequest",description = "운동 그룹 업데이트 요청 DTO")
    public static class Request {

        @Schema(description = "운동 그룹 제목", example = "Morning Workout Group")
        private String title;

        @Schema(description = "운동 그룹 설명", example = "Group for morning exercises")
        private String description;

        @Schema(description = "최대 멤버 수", example = "20")
        private Long maxMember;
    }

    @Getter
    @Setter
    @Builder
    @Schema(name="UpdateWorkOutGroupResponse",description = "운동 그룹 업데이트 응답 DTO")
    public static class Response {

        @Schema(description = "운동 그룹 ID", example = "1")
        private Long groupId;

        @Schema(description = "운동 그룹 제목", example = "Morning Workout Group")
        private String title;

        @Schema(description = "운동 그룹 설명", example = "Group for morning exercises")
        private String description;

        @Schema(description = "업데이트된 최대 멤버 수", example = "20")
        private Long upDatedMaxMember;

        public static Response from(UpdateWorkOutGroupDto dto) {
            return Response.builder()
                    .title(dto.getTitle())
                    .groupId(dto.getGroupId())
                    .description(dto.getDescription())
                    .upDatedMaxMember(dto.getUpDatedMaxMember())
                    .build();
        }
    }
}
