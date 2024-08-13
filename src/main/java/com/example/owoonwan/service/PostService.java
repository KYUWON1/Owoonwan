package com.example.owoonwan.service;

import com.example.owoonwan.domain.Post;
import com.example.owoonwan.dto.dto.CreatePostDto;
import com.example.owoonwan.dto.dto.GetPostDto;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.repository.jpa.PostRepository;
import com.example.owoonwan.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final int PAGE_SIZE = 10;

    @Transactional
    public CreatePostDto createPost(String userId, String comment) {
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(comment);

        Post result = postRepository.save(post);

        return CreatePostDto.builder()
                .userId(result.getUserId())
                .postId(result.getPostId())
                .build();
    }

    //@Cacheable(key = "#postId", value = "postCache")
    @Transactional
    public GetPostDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(ErrorCode.POST_NOT_FOUND));

        return GetPostDto.FromEntity(post);
    }

    @Transactional
    public List<GetPostDto> getPosts(int pageNo, String criteria) {
        Pageable pageable = PageRequest.of(pageNo,PAGE_SIZE,
                Sort.by(Sort.Direction.DESC,criteria));

        Page<Post> posts = postRepository.findAll(pageable);
        if(posts.isEmpty()){
            throw new PostException(ErrorCode.ZERO_POST);
        }

        return posts.stream()
                .map(GetPostDto::FromEntity)
                .collect(Collectors.toList());
    }
}
