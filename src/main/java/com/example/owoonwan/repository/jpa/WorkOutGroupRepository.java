package com.example.owoonwan.repository.jpa;

import com.example.owoonwan.domain.WorkOutGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOutGroupRepository extends JpaRepository<WorkOutGroup,
        Long> {

}
