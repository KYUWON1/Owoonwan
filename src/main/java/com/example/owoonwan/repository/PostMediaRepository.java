package com.example.owoonwan.repository;

import com.example.owoonwan.domain.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia,Long> {
}
