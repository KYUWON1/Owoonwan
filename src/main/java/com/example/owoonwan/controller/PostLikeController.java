package com.example.owoonwan.controller;

import com.example.owoonwan.dto.dto.GetPostLikeInfoDto;
import com.example.owoonwan.dto.response.CreateLikeAndCancelResponse;
import com.example.owoonwan.dto.response.GetPostLikeInfoResponse;
import com.example.owoonwan.service.PostLikeService;
import com.example.owoonwan.utils.UserIdHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/posts/{postId}/likes")
@Tag(name = "Like API", description = "게시물 좋아요 관리 API")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    @Operation(summary = "게시물 좋아요 및 취소", description = "게시물에 대해 좋아요를 누르거나 취소합니다.")
    public CreateLikeAndCancelResponse createLikeAndCancel(
            @Parameter(description = "게시물 ID") @PathVariable Long postId){
        return CreateLikeAndCancelResponse.from(postLikeService.createLike(postId,
                UserIdHolder.getUserIdFromToken()));
    }

    @GetMapping
    @Operation(summary = "게시물 좋아요 개수 조회", description = "특정 게시물의 좋아요 개수를 조회합니다.")
    public Long getPostLikeCount(
            @Parameter(description = "게시물 ID") @PathVariable Long postId){
        return postLikeService.getLikeCount(postId);
    }

    @GetMapping("/info")
    @Operation(summary = "게시물 좋아요 정보 조회", description = "특정 게시물에 좋아요를 누른 사용자들의 정보를 조회합니다.")
    public List<GetPostLikeInfoResponse> getPostLikeInfo(
            @Parameter(description = "게시물 ID") @PathVariable Long postId){
        return postLikeService.getPostLikeInfo(postId).stream()
                .map(GetPostLikeInfoResponse::from)
                .collect(Collectors.toList());

    }
}
