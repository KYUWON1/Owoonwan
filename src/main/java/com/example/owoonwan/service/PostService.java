package com.example.owoonwan.service;

import com.example.owoonwan.domain.Post;
import com.example.owoonwan.dto.dto.CreatePostDto;
import com.example.owoonwan.dto.dto.GetPostDto;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.repository.jpa.PostRepository;
import com.example.owoonwan.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final int PAGE_SIZE = 10;
    private final RedisTemplate redisTemplate;

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

    @Transactional
    public GetPostDto getPostDetail(Long postId) {
        String cacheKey = "postCache:" + postId;
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();

        // 캐시 검색
        GetPostDto cachedPost = (GetPostDto) valueOps.get(cacheKey);
        if (cachedPost != null) {
            redisTemplate.expire(cacheKey,Duration.ofMinutes(10));
            log.info("Cache hit for post ID {}. TTL reset to 10 minutes.", postId);
            return cachedPost;
        }

        // 캐시 미스
        log.info("Cache miss for post ID {}, querying database...", postId);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(ErrorCode.POST_NOT_FOUND));
        GetPostDto postDto = GetPostDto.FromEntity(post);

        // 캐싱
        valueOps.set(cacheKey, postDto);
        return postDto;
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
