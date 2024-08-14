package com.example.owoonwan.controller;

import com.example.owoonwan.dto.CreatePostDto;
import com.example.owoonwan.dto.response.CreatePostResponse;
import com.example.owoonwan.dto.SavePostMediaDto;

import com.example.owoonwan.exception.MediaException;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.service.PostMediaService;
import com.example.owoonwan.service.PostService;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.utils.UserIdHolder;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final PostMediaService postMediaService;
    private final static Long MAX_FILE_COUNT = 10L;

    @PostMapping
    public CreatePostResponse createPost(
            @RequestPart(required = false) List<MultipartFile> files,
            @RequestPart String content
    ) {
        if(files != null && !files.isEmpty()){
            checkFileExtension(files);
        }

        SavePostMediaDto media;
        String userId = UserIdHolder.getUserIdFromToken();
        CreatePostDto post = postService.createPost(userId, content);

        // 미디어파일이 없을경우
        if (files == null || files.isEmpty()) {
            return CreatePostResponse.builder()
                    .userId(post.getUserId())
                    .postId(post.getPostId())
                    .build();
        }

        // 파일이 존재할 경우 최대 10개만 등록가능
        if (files.size() > MAX_FILE_COUNT) {
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

    private void checkFileExtension(List<MultipartFile> files){
        for(MultipartFile file : files){
            String fileName = file.getOriginalFilename();
            int lastDotIndex = fileName.lastIndexOf(".");
            if(lastDotIndex == -1 || !isValidExtension(fileName.substring(lastDotIndex+1).toLowerCase())){
                throw new MediaException(ErrorCode.FILE_EXTENSION_UNKNOWN);
            }
        }
    }

    private boolean isValidExtension(String extension){
        return IMAGE_EXTENSIONS.contains(extension.toLowerCase()) || VIDEO_EXTENSIONS.contains(extension.toLowerCase());
    }

    private static final List<String> IMAGE_EXTENSIONS = List.of(
            "jpg", "jpeg", "png", "gif", "bmp", "tif", "tiff", "webp", "svg", "heic"
    );

    private static final List<String> VIDEO_EXTENSIONS = List.of(
            "mp4", "avi", "mkv", "mov", "wmv", "flv", "webm", "mpeg", "mpg", "3gp",
            "ogg", "m4v", "asf", "vob", "rmvb"
    );
}

