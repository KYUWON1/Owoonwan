package com.example.owoonwan.controller;

import com.example.owoonwan.dto.dto.*;
import com.example.owoonwan.dto.response.CreatePostResponse;
import com.example.owoonwan.dto.response.DeletePostResponse;
import com.example.owoonwan.dto.response.GetPostMediaResponse;
import com.example.owoonwan.dto.response.UpdatePostResponse;
import com.example.owoonwan.exception.MediaException;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.repository.jpa.PostRepository;
import com.example.owoonwan.service.PostMediaService;
import com.example.owoonwan.service.PostService;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.type.MediaType;
import com.example.owoonwan.utils.UserIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
            return CreatePostResponse.from(post);
        }

        // 파일이 존재할 경우 최대 10개만 등록가능
        if (files.size() > MAX_FILE_COUNT) {
            throw new PostException(ErrorCode.MAX_POST_SIZE_10);
        }

        try {
            media = postMediaService.savePostMedia(post.getPostId(), files);
        } catch (IOException e) {
            throw new PostException(ErrorCode.S3_PUT_EXCEPTION);
        }

        return CreatePostResponse.from(post,media);
    }

    @GetMapping
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

    @GetMapping("/{postId}")
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

    @PatchMapping("/{postId}")
    public UpdatePostResponse updatePost(
            @PathVariable Long postId,
            @RequestPart(required = false) List<MultipartFile> files,
            @RequestPart String content
    ){
        if(files != null && !files.isEmpty()){
            checkFileExtension(files);
        }

        String userId = UserIdHolder.getUserIdFromToken();
        UpdatePostDto post = postService.updatePost(content, userId, postId);

        // 미디어파일이 없을경우, 해당 게시글의 미디어 파일 삭제
        if (files == null || files.isEmpty()) {
            postMediaService.deletePostMedia(postId);
            return UpdatePostResponse.from(post);
        }
        // 미디어 파일이 있을경우,
        SavePostMediaDto media;
        // 파일이 존재할 경우 최대 10개만 등록가능
        if (files.size() > MAX_FILE_COUNT) {
            throw new PostException(ErrorCode.MAX_POST_SIZE_10);
        }
        try {
            media = postMediaService.updatePostMedia(post.getPostId(), files);
        } catch (IOException e) {
            throw new PostException(ErrorCode.S3_PUT_EXCEPTION);
        }

        return UpdatePostResponse.from(post,media);
    }

    @DeleteMapping("/{postId}")
    public DeletePostResponse deletePost(
            @PathVariable Long postId
    ){
        String userId = UserIdHolder.getUserIdFromToken();
        DeletePostDto deletePostDto = postService.deletePost(postId, userId);
        postMediaService.deletePostMedia(postId);

        return DeletePostResponse.from(deletePostDto,userId);
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