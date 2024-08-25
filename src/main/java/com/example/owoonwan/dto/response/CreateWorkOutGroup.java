package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.CreateWorkOutDto;
import com.example.owoonwan.type.GroupStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class CreateWorkOutGroup {

    @Getter
    @Setter
    @Schema(description = "운동 그룹 생성 요청 DTO")
    public static class Request {
        @Schema(description = "최대 회원 수", example = "20")
        private Long maxMember;

        @Schema(description = "그룹 제목", example = "Morning Workout Group")
        private String title;

        @Schema(description = "그룹 설명", example = "A group for morning workout enthusiasts.")
        private String description;
    }

    @Getter
    @Setter
    @Builder
    @Schema(description = "운동 그룹 생성 응답 DTO")
    public static class Response {
        @Schema(description = "그룹 소유자 ID", example = "owner123")
        private String ownerId;

        @Schema(description = "최대 회원 수", example = "20")
        private Long maxMember;

        @Schema(description = "그룹 제목", example = "Morning Workout Group")
        private String title;

        @Schema(description = "그룹 상태", example = "ACTIVE")
        private GroupStatus status;

        @Schema(description = "그룹 생성일자", example = "2024-08-25T10:15:30")
        private Date createdAt;

        public static Response from(CreateWorkOutDto dto) {
            return Response.builder()
                    .ownerId(dto.getOwnerId())
                    .title(dto.getTitle())
                    .maxMember(dto.getMaxMember())
                    .createdAt(dto.getCreatedAt())
                    .status(dto.getStatus())
                    .build();
        }
    }
}
