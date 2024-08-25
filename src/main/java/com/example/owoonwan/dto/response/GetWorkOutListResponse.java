package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.GetWorkOutListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "운동 그룹 목록 응답 DTO")
public class GetWorkOutListResponse {

    @Schema(description = "운동 그룹 리스트")
    private List<GetWorkOutListDto> groupList;
}
