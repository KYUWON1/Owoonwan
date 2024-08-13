package com.example.owoonwan.repository.jpa;

import com.example.owoonwan.domain.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia,Long> {
    List<PostMedia> findAllByPostId(Long postId);
}
