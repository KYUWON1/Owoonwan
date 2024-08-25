package com.example.owoonwan.controller;

import com.example.owoonwan.dto.dto.GetWorkOutListDto;
import com.example.owoonwan.dto.dto.LeaveWorkOutGroupDto;
import com.example.owoonwan.dto.response.*;
import com.example.owoonwan.service.WorkOutGroupService;
import com.example.owoonwan.utils.UserIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workout-group")
@Slf4j
@Tag(name = "WorkOut Group API", description = "운동 그룹 관리 API")
public class WorkOutGroupController {
    private final WorkOutGroupService workOutGroupService;

    @PostMapping
    @Operation(summary = "운동 그룹 생성", description = "새로운 운동 그룹을 생성합니다.")
    public CreateWorkOutGroup.Response createWorkOutGroup(
            @RequestBody @Parameter(description = "운동 그룹 생성 요청 정보") CreateWorkOutGroup.Request request
    ){
        return CreateWorkOutGroup.Response.from(workOutGroupService.createWorkOutGroup(request,
                UserIdHolder.getUserIdFromToken()));
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "운동 그룹 상세 조회", description = "특정 운동 그룹의 상세 정보를 조회합니다.")
    public GetWorkOutListDto getWorkOutGroupDetail(
            @Parameter(description = "운동 그룹 ID") @PathVariable Long groupId
    ){
        return workOutGroupService.getWorkOutGroupDetail(groupId);
    }

    @GetMapping
    @Operation(summary = "운동 그룹 목록 조회", description = "전체 운동 그룹의 목록을 조회합니다.")
    public List<GetWorkOutListDto> getWorkOutGroupList(){
        return workOutGroupService.getWorkOutGroupList();
    }

    @PatchMapping("/{groupId}")
    @Operation(summary = "운동 그룹 정보 수정", description = "특정 운동 그룹의 정보를 수정합니다." +
            "현재인원수 미만으로 수정 불가합니다.")
    public UpdateWorkOutGroup.Response updateWorkOutGroup(
            @Parameter(description = "운동 그룹 ID") @PathVariable Long groupId,
            @RequestBody @Parameter(description = "운동 그룹 수정 요청 정보") UpdateWorkOutGroup.Request request
    ){
        return UpdateWorkOutGroup.Response.from(workOutGroupService.updateWorkOutGroup(
                groupId,
                UserIdHolder.getUserIdFromToken(),
                request
        ));
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "운동 그룹 삭제", description = "특정 운동 그룹을 삭제합니다.")
    public DeleteWorkOutGroupResponse deleteWorkOutGroup(
            @Parameter(description = "운동 그룹 ID") @PathVariable Long groupId
    ){
        return DeleteWorkOutGroupResponse.from(workOutGroupService.deleteWorkOutGroup(groupId,
                UserIdHolder.getUserIdFromToken()));
    }

    @PostMapping("/{groupId}/join")
    @Operation(summary = "운동 그룹 참여", description = "특정 운동 그룹에 참여합니다." +
            "정원이 가득차있으면 불가합니다.")
    public JoinWorkOutResponse joinWorkOutGroup(
            @Parameter(description = "운동 그룹 ID") @PathVariable Long groupId
    ){
        return JoinWorkOutResponse
                .from(workOutGroupService.joinWorkOutGroup(groupId, UserIdHolder.getUserIdFromToken()));

    }

    @DeleteMapping("/{groupId}/leave")
    @Operation(summary = "운동 그룹 탈퇴", description = "특정 운동 그룹에서 탈퇴합니다.")
    public LeaveWorkOutGroupResponse leaveWorkOutGroup(
            @Parameter(description = "운동 그룹 ID") @PathVariable Long groupId
    ){
        return LeaveWorkOutGroupResponse.from(workOutGroupService.leaveWorkOutGroup(groupId,
                UserIdHolder.getUserIdFromToken()));
    }
}
