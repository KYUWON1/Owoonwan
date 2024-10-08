package com.example.owoonwan.repository.jpa;

import com.example.owoonwan.domain.WorkOutGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOutGroupMemberRepository extends JpaRepository<WorkOutGroupMember,Long> {
    List<WorkOutGroupMember> findByGroupId(Long groupId);
    Optional<WorkOutGroupMember> findByGroupIdAndUserId(Long groupId,
                                                       String userId);

    @Modifying
    @Query("DELETE FROM WorkOutGroupMember gm WHERE gm.groupId = :groupId")
    void deleteByGroupId(Long groupId);
}
