package com.example.owoonwan.repository.jpa;

import com.example.owoonwan.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
}
