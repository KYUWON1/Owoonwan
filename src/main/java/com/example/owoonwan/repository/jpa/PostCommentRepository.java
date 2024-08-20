package com.example.owoonwan.repository.jpa;

import com.example.owoonwan.domain.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment,Long> {
    Page<PostComment> findByPostId(Long postId, Pageable pageable);
}
