package com.example.owoonwan.controller;

import com.example.owoonwan.dto.response.CreateLikeAndCancelResponse;
import com.example.owoonwan.service.PostLikeService;
import com.example.owoonwan.utils.UserIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/posts/{postId}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public CreateLikeAndCancelResponse createLikeAndCancel(@PathVariable Long postId){
        return CreateLikeAndCancelResponse.from(postLikeService.createLike(postId,
                UserIdHolder.getUserIdFromToken()));
    }

    @GetMapping
    public Long getPostLikeCount(@PathVariable Long postId){
        return postLikeService.getLikeCount(postId);
    }
}
