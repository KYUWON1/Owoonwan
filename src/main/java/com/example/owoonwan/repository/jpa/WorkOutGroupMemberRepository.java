package com.example.owoonwan.repository.jpa;

import com.example.owoonwan.domain.WorkOutGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOutGroupMemberRepository extends JpaRepository<WorkOutGroupMember,Long> {
}
