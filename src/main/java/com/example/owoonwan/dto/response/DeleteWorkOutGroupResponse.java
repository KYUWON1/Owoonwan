package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.DeleteWorkOutGroupDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "운동 그룹 삭제 응답 DTO")
public class DeleteWorkOutGroupResponse {

    @Schema(description = "삭제된 운동 그룹 ID", example = "101")
    private Long groupId;

    public static DeleteWorkOutGroupResponse from(DeleteWorkOutGroupDto dto) {
        return new DeleteWorkOutGroupResponse(dto.getGroupId());
    }
}
