package com.example.owoonwan.controller;

import com.example.owoonwan.dto.CreatePostDto;
import com.example.owoonwan.dto.response.CreatePostResponse;
import com.example.owoonwan.dto.SavePostMediaDto;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.service.PostMediaService;
import com.example.owoonwan.service.PostService;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    ){
        SavePostMediaDto media;
        String userId = SecurityUtils.getUserIdFromToken();
        CreatePostDto post = postService.createPost(userId, comment);
        
        // 댓글만 게시할 경우
        if(files == null) {
            return CreatePostResponse.builder()
                    .userId(post.getUserId())
                    .postId(post.getPostId())
                    .build();
        }

        // 파일이 존재할 경우 최대 10개만 등록가능
        if(files.size() > maxLen){
            throw new PostException(ErrorCode.MAX_POST_SIZE_10);
        }

        try{
            media = postMediaService.savePostMedia(post.getPostId(), files);
        }catch (IOException e){
            throw new PostException(ErrorCode.S3_PUT_EXCEPTION);
        }

        return CreatePostResponse.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .mediaId(media.getMediaId())
                .url(media.getUrl())
                .build();
    }
}
