package com.example.owoonwan.service;

import com.example.owoonwan.domain.WorkOutGroup;
import com.example.owoonwan.dto.dto.CreateWorkOutDto;
import com.example.owoonwan.dto.response.CreateWorkOutGroup;
import com.example.owoonwan.repository.jpa.WorkOutGroupRepository;
import com.example.owoonwan.type.GroupStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkOutGroupService {
    private final WorkOutGroupRepository workOutGroupRepository;
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

}
