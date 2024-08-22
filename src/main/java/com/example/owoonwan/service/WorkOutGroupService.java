package com.example.owoonwan.service;

import com.example.owoonwan.aop.GroupLock;
import com.example.owoonwan.domain.WorkOutGroup;
import com.example.owoonwan.domain.WorkOutGroupMember;
import com.example.owoonwan.dto.dto.*;
import com.example.owoonwan.dto.response.CreateWorkOutGroup;
import com.example.owoonwan.dto.response.UpdateWorkOutGroup;
import com.example.owoonwan.exception.WorkOutGroupException;
import com.example.owoonwan.repository.jpa.WorkOutGroupMemberRepository;
import com.example.owoonwan.repository.jpa.WorkOutGroupRepository;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.type.GroupStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOutGroupService {
    private final WorkOutGroupRepository workOutGroupRepository;
    private final WorkOutGroupMemberRepository workOutGroupMemberRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public CreateWorkOutDto createWorkOutGroup(
            CreateWorkOutGroup.Request request,
            String userId
    ) {
        WorkOutGroup group = new WorkOutGroup();
        group.setOwnerId(userId);
        group.setMaxMember(request.getMaxMember());
        group.setTitle(request.getTitle());
        group.setDescription(request.getDescription());
        group.setStatus(GroupStatus.POSSIBLE);

        return CreateWorkOutDto.fromDomain(workOutGroupRepository.save(group));
    }

    @Transactional
    public JoinWorkOutGroupDto joinWorkOutGroup(Long groupId, String userId) {
        WorkOutGroup group = workOutGroupRepository.findById(groupId)
                .orElseThrow(()-> new WorkOutGroupException(ErrorCode.GROUP_NOT_FOUND));

        return getGroupIdLockAndJoin(groupId, group, userId);
    }

    @Transactional
    public List<GetWorkOutListDto> getWorkOutGroupList() {
        List<GetWorkOutListDto> results = new ArrayList<>();

        List<WorkOutGroup> groups = workOutGroupRepository.findAll();
        for(WorkOutGroup group : groups){
            List<WorkOutGroupMember> groupMember =
                    workOutGroupMemberRepository.findByGroupId(group.getGroupId());
            results.add(GetWorkOutListDto.fromDomain(group,groupMember));
        }
        return results;
    }

    @Transactional
    public GetWorkOutListDto getWorkOutGroupDetail(Long groupId) {
        WorkOutGroup group = workOutGroupRepository.findById(groupId)
                .orElseThrow(()-> new WorkOutGroupException(ErrorCode.GROUP_NOT_FOUND));
        List<WorkOutGroupMember> groupMember =
                workOutGroupMemberRepository.findByGroupId(group.getGroupId());
        return GetWorkOutListDto.fromDomain(group,groupMember);
    }

    @Transactional
    public UpdateWorkOutGroupDto updateWorkOutGroup(Long groupId, String userId,
                                   UpdateWorkOutGroup.Request request) {
        WorkOutGroup group = workOutGroupRepository.findById(groupId)
                .orElseThrow(()-> new WorkOutGroupException(ErrorCode.GROUP_NOT_FOUND));
        // 모임 개설자 검증
        if(!group.getOwnerId().equals(userId)){
            throw new WorkOutGroupException(ErrorCode.USER_INFO_UN_MATCH);
        }
        // 임계구역을 가능한 작게
        WorkOutGroup updatedGroup = getGroupIdLockAndUpdate(group, request);
        updatedGroup.setTitle(request.getTitle());
        updatedGroup.setDescription(request.getDescription());

        return UpdateWorkOutGroupDto.fromDomain(workOutGroupRepository.save(updatedGroup));
    }

    @Transactional
    public LeaveWorkOutGroupDto leaveWorkOutGroup(Long groupId, String userId) {
        WorkOutGroup group = workOutGroupRepository.findById(groupId)
                .orElseThrow(()-> new WorkOutGroupException(ErrorCode.GROUP_NOT_FOUND));
        WorkOutGroupMember member =
                workOutGroupMemberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new WorkOutGroupException(ErrorCode.USER_NOT_EXIST_IN_GROUP));
        WorkOutGroup updatedGroup = getGroupIdLockAndLeave(group);
        workOutGroupRepository.save(updatedGroup);
        workOutGroupMemberRepository.delete(member);
        return LeaveWorkOutGroupDto.builder()
                .groupId(groupId)
                .userId(userId)
                .build();
    }

    @GroupLock
    private WorkOutGroup getGroupIdLockAndLeave(WorkOutGroup group){
        group.setMemberCount(group.getMemberCount() - 1);
        if(group.getStatus().equals(GroupStatus.FULL)){
            group.setStatus(GroupStatus.POSSIBLE);
        }
        return group;
    }

    @GroupLock
    private WorkOutGroup getGroupIdLockAndUpdate(
            WorkOutGroup group
            ,UpdateWorkOutGroup.Request request){
        Long currMaxMember = group.getMaxMember();
        Long currCount = group.getMemberCount();
        Long fixMaxCount = request.getMaxMember();
        // 현재 인원수 이하로 멤버 감소 불가능
        if(fixMaxCount < currCount){
            throw new WorkOutGroupException(ErrorCode.CANT_REDUCE_MEMBER_COUNT);
        }
        // 인원수 증가는 가능
        if(fixMaxCount >= currMaxMember){
            group.setMaxMember(fixMaxCount);
            // 꽉찬 상태였다면, FULL 그대로, 인원 늘리면 POSSIBLE로 변경
            if(fixMaxCount > currMaxMember){
                group.setStatus(GroupStatus.POSSIBLE);
            }
        }
        return group;
    }

    @GroupLock
    private JoinWorkOutGroupDto getGroupIdLockAndJoin(Long groupId,
                                                WorkOutGroup group,String userId){
        Optional<WorkOutGroupMember> existMember =
                workOutGroupMemberRepository.findByGroupIdAndUserId(groupId, userId);
        if(existMember.isPresent()){
            throw new WorkOutGroupException(ErrorCode.USER_ID_EXIST);
        }
        // status 를 확인할때, 상호배제 필요
        if(group.getStatus().equals(GroupStatus.FULL)){
            throw new WorkOutGroupException(ErrorCode.GROUP_IS_FULL);
        }
        JoinWorkOutGroupDto result = new JoinWorkOutGroupDto();
        Long count = group.getMemberCount();
        Long max = group.getMaxMember();
        // 참여 가능하다면
        WorkOutGroupMember member = new WorkOutGroupMember();
        member.setUserId(userId);
        member.setGroupId(groupId);
        workOutGroupMemberRepository.save(member);

        group.setMemberCount(++count);
        if(count.equals(max)){
            group.setStatus(GroupStatus.FULL);
        }
        workOutGroupRepository.save(group);

        result.setTitle(group.getTitle());
        result.setGroupId(groupId);
        result.setUserId(userId);

        return result;
    }


}
