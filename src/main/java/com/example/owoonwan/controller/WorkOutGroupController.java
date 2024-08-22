package com.example.owoonwan.controller;

import com.example.owoonwan.dto.dto.GetWorkOutListDto;
import com.example.owoonwan.dto.dto.LeaveWorkOutGroupDto;
import com.example.owoonwan.dto.response.*;
import com.example.owoonwan.service.WorkOutGroupService;
import com.example.owoonwan.utils.UserIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workout-group")
@Slf4j
public class WorkOutGroupController {
    private final WorkOutGroupService workOutGroupService;

    @PostMapping
    public CreateWorkOutGroup.Response createWorkOutGroup(
            @RequestBody CreateWorkOutGroup.Request request
    ){
        return CreateWorkOutGroup.Response.from(workOutGroupService.createWorkOutGroup(request,
                UserIdHolder.getUserIdFromToken()));
    }

    @PatchMapping("/{groupId}")
    public UpdateWorkOutGroup.Response updateWorkOutGroup(
            @PathVariable Long groupId,
            @RequestBody UpdateWorkOutGroup.Request request
    ){
        return UpdateWorkOutGroup.Response.from(workOutGroupService.updateWorkOutGroup(
                groupId,
                UserIdHolder.getUserIdFromToken(),
                request
        ));
    }

    @DeleteMapping("/{groupId}")
    public DeleteWorkOutGroupResponse deleteWorkOutGroup(
            @PathVariable Long groupId
    ){
        return DeleteWorkOutGroupResponse.from(workOutGroupService.deleteWorkOutGroup(groupId,
                UserIdHolder.getUserIdFromToken()));
    }

    @GetMapping("/{groupId}")
    public GetWorkOutListDto getWorkOutGroupDetail(
            @PathVariable Long groupId
    ){
        return workOutGroupService.getWorkOutGroupDetail(groupId);
    }

    @GetMapping
    public List<GetWorkOutListDto> getWorkOutGroupList(){
       return workOutGroupService.getWorkOutGroupList();
    }

    @PostMapping("/{groupId}/join")
    public JoinWorkOutResponse joinWorkOutGroup(
            @PathVariable Long groupId
    ){
        return JoinWorkOutResponse
                .from(workOutGroupService.joinWorkOutGroup(groupId, UserIdHolder.getUserIdFromToken()));

    }

    @DeleteMapping("/{groupId}/leave")
    public LeaveWorkOutGroupResponse leaveWorkOutGroup(
            @PathVariable Long groupId
    ){
        return LeaveWorkOutGroupResponse.from(workOutGroupService.leaveWorkOutGroup(groupId,
                UserIdHolder.getUserIdFromToken()));
    }


}
