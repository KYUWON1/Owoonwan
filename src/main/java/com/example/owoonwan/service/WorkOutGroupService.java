package com.example.owoonwan.service;

import com.example.owoonwan.aop.GroupJoinLock;
import com.example.owoonwan.domain.WorkOutGroup;
import com.example.owoonwan.domain.WorkOutGroupMember;
import com.example.owoonwan.dto.dto.CreateWorkOutDto;
import com.example.owoonwan.dto.dto.GetWorkOutListDto;
import com.example.owoonwan.dto.dto.JoinWorkOutGroupDto;
import com.example.owoonwan.dto.response.CreateWorkOutGroup;
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
    @GroupJoinLock
    public JoinWorkOutGroupDto joinWorkOutGroup(Long groupId, String userId) {
        JoinWorkOutGroupDto result = new JoinWorkOutGroupDto();

        WorkOutGroup group = workOutGroupRepository.findById(groupId)
                .orElseThrow(()-> new WorkOutGroupException(ErrorCode.GROUP_NOT_FOUND));

        if(group.getStatus().equals(GroupStatus.FULL)){
            throw new WorkOutGroupException(ErrorCode.GROUP_IS_FULL);
        }

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

    public GetWorkOutListDto getWorkOutGroupDetail(Long groupId) {
        WorkOutGroup group = workOutGroupRepository.findById(groupId)
                .orElseThrow(()-> new WorkOutGroupException(ErrorCode.GROUP_NOT_FOUND));
        List<WorkOutGroupMember> groupMember =
                workOutGroupMemberRepository.findByGroupId(group.getGroupId());
        return GetWorkOutListDto.fromDomain(group,groupMember);
    }
}
