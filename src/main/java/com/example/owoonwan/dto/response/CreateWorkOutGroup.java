package com.example.owoonwan.dto.response;

import com.example.owoonwan.domain.WorkOutGroup;
import com.example.owoonwan.dto.dto.CreateWorkOutDto;
import com.example.owoonwan.type.GroupStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class CreateWorkOutGroup {

    @Getter
    @Setter
    public static class Request {
        private Long maxMember;
        private String title;
        private String description;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String ownerId;
        private Long maxMember;
        private String title;
        private GroupStatus status;
        private Date createdAt;

        public static Response from(CreateWorkOutDto dto){
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
