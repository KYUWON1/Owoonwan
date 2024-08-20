package com.example.owoonwan.repository.jpa;

import com.example.owoonwan.domain.PostMedia;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.jpa.repository.query.QueryUtils.DELETE_ALL_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia,Long> {
    List<PostMedia> findAllByPostId(Long postId);

    @Modifying
    @Query("DELETE FROM PostMedia pm WHERE pm.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}
