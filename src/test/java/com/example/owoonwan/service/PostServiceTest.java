package com.example.owoonwan.service;

import com.example.owoonwan.domain.Post;
import com.example.owoonwan.dto.dto.CreatePostDto;
import com.example.owoonwan.dto.dto.PostDtoRedisTemplate;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.repository.jpa.PostRepository;
import com.example.owoonwan.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.data.domain.Pageable;


import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private RedisTemplate<String, PostDtoRedisTemplate> redisTemplate;

    @InjectMocks
    private PostService postService;

    private MockHttpSession session;

    @Test
    @DisplayName("Success createPost")
    void successCreatePost() {
        //given
        String userId = "testId";
        String content = "testContent";

        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);

        given(postRepository.save(any(Post.class)))
                .willReturn(post);
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

        //when
        CreatePostDto result = postService.createPost(userId, content);

        //then
        verify(postRepository,times(1)).save(captor.capture());
        Post captured = captor.getValue();
        assertEquals(userId,captured.getUserId());
        assertEquals(content,captured.getContent());
        assertEquals(result.getUserId(),userId);
    }

    @Test
    @DisplayName("Success getPostDetail - from cache")
    void getPostDetailsFromCache() {
        // given
        Long postId = 1L;
        PostDtoRedisTemplate cachedPostDto = PostDtoRedisTemplate.builder()
                    .postId(postId)
                    .userId("testId")
                    .content("testContent")
                    .build();
        ValueOperations<String, PostDtoRedisTemplate> valueOps = mock(ValueOperations.class);
        given(redisTemplate.opsForValue()).willReturn(valueOps);
        given(valueOps.get("postCache:" + postId)).willReturn(cachedPostDto);

        // when
        PostDtoRedisTemplate result = postService.getPostDetail(postId);

        // then
        verify(redisTemplate).expire(eq("postCache:" + postId), any(Duration.class));
        verify(postRepository, never()).findById(any());
        assertEquals(postId, result.getPostId());
    }

    @Test
    @DisplayName("Success getPostDetail - from DB")
    void getPostDetailsFromDB() {
        // given
        Long postId = 1L;
        Post post = new Post();
        post.setPostId(postId);
        post.setUserId("testId");
        post.setContent("testContent");
        post.setUpdatedAt(new Date());

        ValueOperations<String, PostDtoRedisTemplate> valueOps = mock(ValueOperations.class);
        given(redisTemplate.opsForValue()).willReturn(valueOps);
        given(valueOps.get("postCache:" + postId)).willReturn(null);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        ArgumentCaptor<PostDtoRedisTemplate> captor = ArgumentCaptor.forClass(PostDtoRedisTemplate.class);

        // when
        PostDtoRedisTemplate result = postService.getPostDetail(postId);

        // then
        verify(valueOps).set(eq("postCache:" + postId), captor.capture(),
                any(Duration.class));
        PostDtoRedisTemplate capturedDto = captor.getValue();
        assertNotNull(capturedDto);
        verify(redisTemplate,never()).expire(anyString(),any(Duration.class));
        verify(postRepository, times(1)).findById(postId);
        assertEquals(postId, capturedDto.getPostId());
        assertEquals("testId", capturedDto.getUserId());
        assertEquals("testContent", capturedDto.getContent());
        assertEquals(result,capturedDto);
    }

    @Test
    @DisplayName("Failure getPostDetail - 게시글 존재하지 않음")
    void failureGetPostDetailByPostNotFound() {
        // given
        Long postId = 1L;
        Post post = new Post();
        post.setPostId(postId);
        post.setUserId("testId");
        post.setContent("testContent");
        post.setUpdatedAt(new Date());

        ValueOperations<String, PostDtoRedisTemplate> valueOps = mock(ValueOperations.class);
        given(redisTemplate.opsForValue()).willReturn(valueOps);
        given(valueOps.get("postCache:" + postId)).willReturn(null);
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when
        PostException exception = assertThrows(PostException.class,
                () -> postService.getPostDetail(postId));

        // then
        verify(redisTemplate,never()).expire(anyString(),any(Duration.class));
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository,times(0)).save(any());
        assertEquals(exception.getErrorCode(), ErrorCode.POST_NOT_FOUND);
    }

    @Test
    @DisplayName("successGetPosts")
    void successGetPosts() {
        //given
        Pageable pageable = PageRequest.of(0,10);
        Post post = new Post();
        post.setPostId(1L);
        post.setUserId("testId");
        post.setContent("testContent");
        post.setUpdatedAt(new Date());
        List<Post> postList = Arrays.asList(
                post,
                post
        );
        Page<Post> postPage = new PageImpl<>(postList,pageable,postList.size());
        given(postRepository.findAll(pageable)).willReturn(postPage);

        //when
        List<PostDtoRedisTemplate> result = postService.getPosts(pageable);

        //then
        assertFalse(result.isEmpty());
        assertEquals(2,result.size());
        assertEquals("testId",result.get(0).getUserId());
        assertEquals("testContent",result.get(0).getContent());
        verify(postRepository,times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Failure GetPosts - 게시글 존재 x")
    void failureGetPostsByNoPosts() {
        //given
        Pageable pageable = PageRequest.of(0,10);
        Page<Post> emptyPage = Page.empty();
        given(postRepository.findAll(pageable)).willReturn(emptyPage);

        //when
        PostException exception = assertThrows(PostException.class,
                () -> postService.getPosts(pageable));
        //then
        assertEquals(exception.getErrorCode(),ErrorCode.ZERO_POST);
        verify(postRepository,times(1)).findAll(pageable);
    }
}