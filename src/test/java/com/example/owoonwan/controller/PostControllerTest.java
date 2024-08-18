package com.example.owoonwan.controller;

import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.dto.*;
import com.example.owoonwan.jwt.JwtUtil;
import com.example.owoonwan.service.PostMediaService;
import com.example.owoonwan.service.PostService;
import com.example.owoonwan.service.UserService;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.type.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.owoonwan.type.MediaType.IMAGE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
@Import(JwtUtil.class)
class PostControllerTest {
    @MockBean
    private PostService postService;
    @MockBean
    private PostMediaService postMediaService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final static Long MAX_FILE_COUNT = 10L;

    @BeforeEach
    void setUp() {
        UserDto user = UserDto.builder()
                .userId("testId")
                .password("12341234")
                .role(UserRole.USER)
                .build();
        UserDetailsDto userDetails = new UserDetailsDto(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Success createPost - 미디어 파일 존재")
    void successCreatePostExistMedia() throws Exception {
        //given
        given(postService.createPost(anyString(),anyString()))
                .willReturn(CreatePostDto.builder()
                        .userId("testId")
                        .postId(1234L)
                        .build());

        given(postMediaService.savePostMedia(anyLong(),any()))
                .willReturn(SavePostMediaDto.builder()
                        .mediaId(1234L)
                        .url("s3url")
                        .build());
        MockMultipartFile file = new MockMultipartFile("files", "test.jpg", "image/jpeg", "test data".getBytes());
        MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/posts")
                        .file(file)
                        .file((content))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1234L))
                .andExpect(jsonPath("$.mediaId").value(1234L))
                .andExpect(jsonPath("$.url").value("s3url"))
                .andExpect(jsonPath("$.userId").value("testId"));
    }

    @Test
    @DisplayName("success createPost - 미디어 파일 없음")
    void successCreatePost() throws Exception {
        //given
        given(postService.createPost(anyString(),anyString()))
                .willReturn(CreatePostDto.builder()
                        .userId("testId")
                        .postId(1234L)
                        .build());

        MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/posts")
                        .file((content))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1234L))
                .andExpect(jsonPath("$.userId").value("testId"));
    }

    @Test
    @DisplayName("Failure createPost - 잘못된 파일 확장자")
    void failureCreatePostByFileExtension() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("files", "test.test",
                "image/jpeg", "test data".getBytes());
        MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/posts")
                        .file(file)
                        .file((content))
                        .param("content", "test post content") // Use param to send text data
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("FILE_EXTENSION_UNKNOWN"))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.FILE_EXTENSION_UNKNOWN.getDescription()));
    }

    @Test
    @DisplayName("Failure createPost - 최대 파일 수는 10개")
    void failureCreatePostByFileExceed() throws Exception {
        //given
        given(postService.createPost(anyString(),anyString()))
                .willReturn(CreatePostDto.builder()
                        .userId("testId")
                        .postId(1234L)
                        .build());

        List<MockMultipartFile> files = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            files.add(new MockMultipartFile("files", "test.jpg", "image/jpeg", "test data".getBytes()));
        }
        MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());

        //when & then
        MockMultipartHttpServletRequestBuilder builder = (MockMultipartHttpServletRequestBuilder) MockMvcRequestBuilders.multipart("/api/v1/posts")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("content", "test post content"); // Use param to send text data
        files.forEach(file -> builder.file(file));
        builder.file(content);
        mockMvc.perform(builder.with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("MAX_POST_SIZE_10"))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.MAX_POST_SIZE_10.getDescription()));
    }

    @Test
    @DisplayName("Failure createPost - S3 저장 오류")
    void failureCreatePostS3Exception() throws Exception {
        //given
        given(postService.createPost(anyString(),anyString()))
                .willReturn(CreatePostDto.builder()
                        .userId("testId")
                        .postId(1234L)
                        .build());

        given(postMediaService.savePostMedia(anyLong(),any()))
                .willReturn(SavePostMediaDto.builder()
                        .mediaId(1234L)
                        .url("s3url")
                        .build());
        MockMultipartFile file = new MockMultipartFile("files", "test.jpg", "image/jpeg", "test data".getBytes());
        MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());
        //when
        doThrow(new IOException()).when(postMediaService).savePostMedia(anyLong(),any());
        //then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/posts")
                        .file(file)
                        .file((content))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("S3_PUT_EXCEPTION"))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.S3_PUT_EXCEPTION.getDescription()));
    }

    @Test
    @DisplayName("success getPostList - 미디어 파일 존재시")
    void successGetPostListExistMedia() throws Exception {
        //given
        List<GetPostDto> posts = new ArrayList<>();
        posts.add(GetPostDto.builder()
                        .userId("testId")
                        .postId(1L)
                        .content("test content")
                        .updatedAt(new Date())
                        .build());
        List<GetPostMediaDto.MediaInfo> mediaInfos = new ArrayList<>();
        mediaInfos.add(GetPostMediaDto.MediaInfo.builder()
                        .url("s3url")
                        .type(IMAGE)
                        .sequence(1)
                        .updatedAt(new Date())
                        .build());
        given(postService.getPosts(anyInt(),anyString()))
                .willReturn(posts);

        given(postMediaService.getPostMedium(anyLong()))
                .willReturn(GetPostMediaDto.builder()
                        .postId(1L)
                        .mediaInfos(mediaInfos)
                        .build());

        //then && then
        mockMvc.perform(get("/api/v1/posts")
                        .param("page","0")
                        .param("criteria","createdAt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].userId").value("testId"))
                .andExpect(jsonPath("$.[0].postId").value(1))
                .andExpect(jsonPath("$.[0].content").value("test content"))
                .andExpect(jsonPath("$.[0].mediaInfos[0].url").value("s3url"))
                .andExpect(jsonPath("$.[0].mediaInfos[0].type").value("IMAGE"))
                .andExpect(jsonPath("$.[0].mediaInfos[0].sequence").value(
                        1));
    }

    @Test
    @DisplayName("success getPostList - 미디어 파일 없을시")
    void successGetPostList() throws Exception {
        //given
        List<GetPostDto> posts = new ArrayList<>();
        posts.add(GetPostDto.builder()
                .userId("testId")
                .postId(1L)
                .content("test content")
                .updatedAt(new Date())
                .build());
        given(postService.getPosts(anyInt(),anyString()))
                .willReturn(posts);
        given(postMediaService.getPostMedium(anyLong()))
                .willReturn(GetPostMediaDto.builder()
                        .postId(1L)
                        .mediaInfos(null)
                        .build());

        //then && then
        mockMvc.perform(get("/api/v1/posts")
                        .param("page","0")
                        .param("criteria","createdAt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].userId").value("testId"))
                .andExpect(jsonPath("$.[0].postId").value(1))
                .andExpect(jsonPath("$.[0].content").value("test content"))
                .andExpect(jsonPath("$.[0].mediaInfos").doesNotExist());
    }

    @Test
    @DisplayName("success getPostDetail - 미디어 파일 존재시")
    void successGetPostDetail() throws Exception {
        //given
        List<GetPostMediaDto.MediaInfo> mediaInfos = new ArrayList<>();
        mediaInfos.add(GetPostMediaDto.MediaInfo.builder()
                .url("s3url")
                .type(IMAGE)
                .sequence(1)
                .updatedAt(new Date())
                .build());

        given(postService.getPostDetail(anyLong()))
                .willReturn(GetPostDto.builder()
                        .userId("testId")
                        .postId(1L)
                        .content("test content")
                        .updatedAt(new Date())
                        .build());
        given(postMediaService.getPostMedium(anyLong()))
                .willReturn(GetPostMediaDto.builder()
                        .postId(1L)
                        .mediaInfos(mediaInfos)
                        .build());

        //then && then
        mockMvc.perform(get("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testId"))
                .andExpect(jsonPath("$.postId").value(1))
                .andExpect(jsonPath("$.content").value("test content"))
                .andExpect(jsonPath("$.mediaInfos[0].url").value("s3url"))
                .andExpect(jsonPath("$.mediaInfos[0].type").value("IMAGE"))
                .andExpect(jsonPath("$.mediaInfos[0].sequence").value(
                        1));
    }

    @Test
    @DisplayName("success getPostDetail - 미디어 파일 없을시")
    void successGetPostDetailNoMedia() throws Exception {
        //given
        given(postService.getPostDetail(anyLong()))
                .willReturn(GetPostDto.builder()
                        .userId("testId")
                        .postId(1L)
                        .content("test content")
                        .updatedAt(new Date())
                        .build());
        given(postMediaService.getPostMedium(anyLong()))
                .willReturn(GetPostMediaDto.builder()
                        .postId(1L)
                        .mediaInfos(null)
                        .build());

        //then && then
        mockMvc.perform(get("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testId"))
                .andExpect(jsonPath("$.postId").value(1))
                .andExpect(jsonPath("$.content").value("test content"))
                .andExpect(jsonPath("$.mediaInfos").doesNotExist());
    }

    @Test
    @DisplayName("success deletePost")
    void successDeletePost() throws Exception {
        //given
        given(postService.deletePost(anyLong(),anyString()))
                .willReturn(DeletePostDto.builder()
                        .postId(1L)
                        .deletedAt(new Date())
                        .build());
        given(postMediaService.deletePostMedia(anyLong()))
                .willReturn(deletePostMediaDto.builder()
                        .postId(1L)
                        .deletedAt(new Date())
                        .build());

        //then && then
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testId"))
                .andExpect(jsonPath("$.postId").value(1));
    }

    @Test
    @DisplayName("Success updatePost - 미디어 파일 존재시")
    void successUpdatePostExistMedia() throws Exception {
        //given
        given(postService.updatePost(anyString(),anyString(),anyLong()))
                .willReturn(UpdatePostDto.builder()
                        .userId("testId")
                        .postId(1L)
                        .build());

        given(postMediaService.updatePostMedia(anyLong(),any()))
                .willReturn(SavePostMediaDto.builder()
                        .mediaId(1L)
                        .url("s3url")
                        .build());
        MockMultipartFile file = new MockMultipartFile("files", "test.jpg", "image/jpeg", "test data".getBytes());
        MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/posts/1")
                        .file(file)
                        .file((content))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.mediaId").value(1L))
                .andExpect(jsonPath("$.url").value("s3url"))
                .andExpect(jsonPath("$.userId").value("testId"));
    }

    @Test
    @DisplayName("Success updatePost - 미디어 파일 없을시")
    void successUpdatePosta() throws Exception {
        //given
        given(postService.updatePost(anyString(),anyString(),anyLong()))
                .willReturn(UpdatePostDto.builder()
                        .userId("testId")
                        .postId(1L)
                        .build());

        given(postMediaService.deletePostMedia(anyLong()))
                .willReturn(deletePostMediaDto.builder()
                        .postId(1L)
                        .build());
       MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/posts/1")
                        .file((content))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.mediaId").doesNotExist())
                .andExpect(jsonPath("$.url").doesNotExist())
                .andExpect(jsonPath("$.userId").value("testId"));
    }

    @Test
    @DisplayName("Failure updatePost - 최대 파일수는 10개")
    void failureUpdatePostByFileMax() throws Exception {
        //given
        given(postService.updatePost(anyString(),anyString(),anyLong()))
                .willReturn(UpdatePostDto.builder()
                        .userId("testId")
                        .postId(1234L)
                        .build());

        List<MockMultipartFile> files = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            files.add(new MockMultipartFile("files", "test.jpg", "image/jpeg", "test data".getBytes()));
        }
        MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());

        //when & then
        MockMultipartHttpServletRequestBuilder builder =
                (MockMultipartHttpServletRequestBuilder) MockMvcRequestBuilders.multipart("/api/v1/posts/1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("content", "test post content"); // Use param to send text data
        files.forEach(file -> builder.file(file));
        builder.file(content);
        mockMvc.perform(builder
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("MAX_POST_SIZE_10"))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.MAX_POST_SIZE_10.getDescription()));
    }

    @Test
    @DisplayName("Failure updatePost - 잘못된 파일 확장자")
    void failureUpdatePostByFileExtension() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("files", "test.test",
                "image/jpeg", "test data".getBytes());
        MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());

        //when & then
        MockMultipartHttpServletRequestBuilder builder =
                (MockMultipartHttpServletRequestBuilder) MockMvcRequestBuilders.multipart("/api/v1/posts/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA);
        builder.file(file);
        builder.file(content);
        mockMvc.perform(builder
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("FILE_EXTENSION_UNKNOWN"))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.FILE_EXTENSION_UNKNOWN.getDescription()));
    }

    @Test
    @DisplayName("Failure updatePost - S3 저장 오류")
    void failureUpdatePostS3Exception() throws Exception {
        //given
        given(postService.updatePost(anyString(),anyString(),anyLong()))
                .willReturn(UpdatePostDto.builder()
                        .userId("testId")
                        .postId(1234L)
                        .build());

        given(postMediaService.updatePostMedia(anyLong(),any()))
                .willReturn(SavePostMediaDto.builder()
                        .mediaId(1234L)
                        .url("s3url")
                        .build());
        MockMultipartFile file = new MockMultipartFile("files", "test.jpg", "image/jpeg", "test data".getBytes());
        MockMultipartFile content = new MockMultipartFile("content","","text" +
                "/plain","test post content".getBytes());
        //when
        doThrow(new IOException()).when(postMediaService).updatePostMedia(anyLong(),any());
        //then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/posts/1")
                        .file(file)
                        .file((content))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("S3_PUT_EXCEPTION"))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.S3_PUT_EXCEPTION.getDescription()));
    }
}