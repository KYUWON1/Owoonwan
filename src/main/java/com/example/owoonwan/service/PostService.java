package com.example.owoonwan.service;

import com.example.owoonwan.domain.Post;
import com.example.owoonwan.dto.dto.CreatePostDto;
import com.example.owoonwan.dto.dto.DeletePostDto;
import com.example.owoonwan.dto.dto.PostDtoRedisTemplate;
import com.example.owoonwan.dto.dto.UpdatePostDto;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.exception.VerifyException;
import com.example.owoonwan.repository.jpa.PostRepository;
import com.example.owoonwan.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final RedisTemplate<String, PostDtoRedisTemplate> redisTemplate;

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
    public PostDtoRedisTemplate getPostDetail(Long postId) {
        String cacheKey = "postCache:" + postId;
        ValueOperations<String, PostDtoRedisTemplate> valueOps = redisTemplate.opsForValue();

        // 캐시 검색
        PostDtoRedisTemplate cachedPost = valueOps.get(cacheKey);
        if (cachedPost != null) {
            redisTemplate.expire(cacheKey,Duration.ofMinutes(10));
            log.info("Cache hit for post ID {}. TTL reset to 10 minutes.", postId);
            return cachedPost;
        }

        // 캐시 미스
        log.info("Cache miss for post ID {}, querying database...", postId);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(ErrorCode.POST_NOT_FOUND));
        PostDtoRedisTemplate postDto = PostDtoRedisTemplate.FromEntity(post);

        // 캐싱
        valueOps.set(cacheKey, postDto,Duration.ofMinutes(10));
        return postDto;
    }

    @Transactional
    public List<PostDtoRedisTemplate> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        if(posts.isEmpty()){
            throw new PostException(ErrorCode.ZERO_POST);
        }

        return posts.stream()
                .map(PostDtoRedisTemplate::FromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeletePostDto deletePost(Long postId, String userId) {
        // 캐시 삭제
        deleteRedisCache(postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        if(!post.getUserId().equals(userId)){
            throw new VerifyException(ErrorCode.USER_INFO_UN_MATCH);
        }

        postRepository.delete(post);

        return DeletePostDto.builder()
                .postId(post.getPostId())
                .deletedAt(new Date())
                .build();
    }

    @Transactional
    public UpdatePostDto updatePost(String content, String userId,
                                    Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
        
        if(!post.getUserId().equals(userId)){
            throw new VerifyException(ErrorCode.USER_INFO_UN_MATCH);
        
        }
        post.setContent(content);
        Post save = postRepository.save(post);

        // Post 캐시 갱신
        deleteRedisCache(postId);
        putRedisCache(postId,save);

        return UpdatePostDto.builder()
                .userId(save.getUserId())
                .postId(save.getPostId())
                .build();
    }

    private void deleteRedisCache(Long postId){
        String cacheKey = "postCache:" + postId;
        redisTemplate.delete(cacheKey);
    }

    private void putRedisCache(Long postId,Post post){
        String cacheKey = "postCache:" + postId;
        redisTemplate.opsForValue().set(cacheKey, PostDtoRedisTemplate.FromEntity(post),Duration.ofMinutes(10));
    }
}
