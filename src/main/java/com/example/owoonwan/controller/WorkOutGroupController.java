package com.example.owoonwan.controller;

import com.example.owoonwan.dto.dto.CreateWorkOutDto;
import com.example.owoonwan.dto.response.CreateWorkOutGroup;
import com.example.owoonwan.service.WorkOutGroupService;
import com.example.owoonwan.utils.UserIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
