package com.example.owoonwan.service;

import com.example.owoonwan.domain.Post;
import com.example.owoonwan.dto.CreatePostDto;
import com.example.owoonwan.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public CreatePostDto createPost(String userId, String content) {
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);

        Post result = postRepository.save(post);

        return CreatePostDto.builder()
                .userId(result.getUserId())
                .postId(result.getPostId())
                .build();
    }
}
