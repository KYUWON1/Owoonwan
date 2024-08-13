package com.example.owoonwan.controller;

import com.example.owoonwan.dto.dto.CreatePostDto;
import com.example.owoonwan.dto.dto.GetPostDto;
import com.example.owoonwan.dto.dto.GetPostMediaDto;
import com.example.owoonwan.dto.response.CreatePostResponse;
import com.example.owoonwan.dto.dto.SavePostMediaDto;
import com.example.owoonwan.dto.response.GetPostMediaResponse;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.service.PostMediaService;
import com.example.owoonwan.service.PostService;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMediaService postMediaService;
    private final Long maxLen = 10L;

    @PostMapping("/post/create")
    public CreatePostResponse createPost(
            @RequestParam(required = false) List<MultipartFile> files,
            @RequestParam String comment
    ) {
        SavePostMediaDto media;
        String userId = SecurityUtils.getUserIdFromToken();
        CreatePostDto post = postService.createPost(userId, comment);

        // 댓글만 게시할 경우
        if (files == null) {
            return CreatePostResponse.builder()
                    .userId(post.getUserId())
                    .postId(post.getPostId())
                    .build();
        }

        // 파일이 존재할 경우 최대 10개만 등록가능
        if (files.size() > maxLen) {
            throw new PostException(ErrorCode.MAX_POST_SIZE_10);
        }

        try {
            media = postMediaService.savePostMedia(post.getPostId(), files);
        } catch (IOException e) {
            throw new PostException(ErrorCode.S3_PUT_EXCEPTION);
        }

        return CreatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .mediaId(media.getMediaId())
                .url(media.getUrl())
                .build();
    }

    @GetMapping("/post")
    public List<GetPostMediaResponse> getPostList(
            @RequestParam(required = false,defaultValue = "0",value="page") int pageNo,
            @RequestParam(required = false,defaultValue = "createdAt",
                    value = "criteria" ) String criteria
    ) {
        List<GetPostDto> posts = postService.getPosts(pageNo, criteria);
        List<GetPostMediaResponse> responseList = new ArrayList<>();

        for(GetPostDto post : posts){
            GetPostMediaDto mediaInPost = postMediaService.getPostMedium(post.getPostId());

            GetPostMediaResponse response = new GetPostMediaResponse();
            if(mediaInPost.getMediaInfos() == null || mediaInPost.getMediaInfos().isEmpty()){
                response = response.fromNoMedia(post);
            }else{
                response = response.fromExistMedia(post,
                        mediaInPost.getMediaInfos());
            }
            System.out.println("response: "+response.getContent());
            responseList.add(response);
        }

        return responseList;
    }

    @GetMapping("/post/{postId}")
    public GetPostMediaResponse getPostDetail(
            @PathVariable Long postId
    ) {
        GetPostDto post = postService.getPostDetail(postId);
        GetPostMediaDto medium =
                postMediaService.getPostMedium(post.getPostId());
        // 미디어 파일 없을시
        if(medium.getMediaInfos() == null || medium.getMediaInfos().isEmpty()){
            return GetPostMediaResponse.fromNoMedia(post);
        }
        // 미디어 파일 존재시
        return GetPostMediaResponse.fromExistMedia(post,medium.getMediaInfos());
    }

}