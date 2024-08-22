package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.UpdateWorkOutGroupDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UpdateWorkOutGroup {
    @Getter
    @Setter
    public static class Request {
        private String title;
        private String description;
        private Long maxMember;
    }
    @Getter
    @Setter
    @Builder
    public static class Response {
        private Long groupId;
        private String title;
        private String description;
        private Long upDatedMaxMember;

        public static Response from(UpdateWorkOutGroupDto dto){
            return Response.builder()
                    .title(dto.getTitle())
                    .groupId(dto.getGroupId())
                    .description(dto.getDescription())
                    .upDatedMaxMember(dto.getUpDatedMaxMember())
                    .build();
        }
    }
}
