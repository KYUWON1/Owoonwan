package com.example.owoonwan.controller;

import com.example.owoonwan.dto.dto.GetPostLikeInfoDto;
import com.example.owoonwan.dto.response.CreateLikeAndCancelResponse;
import com.example.owoonwan.dto.response.GetPostLikeInfoResponse;
import com.example.owoonwan.service.PostLikeService;
import com.example.owoonwan.utils.UserIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/info")
    public List<GetPostLikeInfoResponse> getPostLikeInfo(@PathVariable Long postId){
        return postLikeService.getPostLikeInfo(postId).stream()
                .map(GetPostLikeInfoResponse::from)
                .collect(Collectors.toList());

    }
}
