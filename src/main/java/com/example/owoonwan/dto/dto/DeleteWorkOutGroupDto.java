package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "운동 그룹 삭제 DTO")
public class DeleteWorkOutGroupDto {

    @Schema(description = "삭제할 그룹 ID", example = "123")
    private Long groupId;
}
