package com.example.owoonwan.repository.jpa;

import com.example.owoonwan.domain.Post;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
