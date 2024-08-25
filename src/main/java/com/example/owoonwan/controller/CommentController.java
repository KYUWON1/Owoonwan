package com.example.owoonwan.controller;

import com.example.owoonwan.dto.dto.UpdatePostComment;
import com.example.owoonwan.dto.response.CreateComment;
import com.example.owoonwan.dto.response.DeletePostCommentResponse;
import com.example.owoonwan.dto.response.GetCommentResponse;
import com.example.owoonwan.service.PostCommentService;
import com.example.owoonwan.utils.UserIdHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/comments")
@Slf4j
@Tag(name = "Comment API", description = "댓글 관리 API")
public class CommentController {
    private final PostCommentService postCommentService;

    @PostMapping
    @Operation(summary = "댓글 추가", description = "게시글에 댓글 달기")
    public CreateComment.Response createComments(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @RequestBody CreateComment.Request request
    ) {
        request.setUserId(UserIdHolder.getUserIdFromToken());
        request.setPostId(postId);
        return CreateComment.Response.from(postCommentService.createComment(request));
    }

    @PatchMapping
    @Operation(summary = "댓글 수정", description = "댓글 작성자만 수정 가능")
    public UpdatePostComment.Response updatePostComments(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @RequestBody UpdatePostComment.Request request
    ) {
        return UpdatePostComment.Response
                .from(postCommentService.updatePostComment(request, UserIdHolder.getUserIdFromToken(), postId));
    }

    @GetMapping
    @Operation(summary = "댓글 목록 불러오기", description = "게시글 댓글을 페이지단위로 불러옵니다.")
    public List<GetCommentResponse> getPostCommentsList(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return postCommentService.getPostComment(postId, pageable).stream()
                .map(GetCommentResponse::from)
                .collect(Collectors.toList());
    }

    @DeleteMapping
    @Operation(summary = "댓글 삭제", description = "댓글 삭제는 게시글 작성자와 댓글 작성자만" +
            "가능")
    public DeletePostCommentResponse deletePostComments(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @Parameter(description = "삭제할 댓글 ID") @RequestParam Long commentId
    ) {
        return DeletePostCommentResponse
                .from(postCommentService.deletePostComment(postId, UserIdHolder.getUserIdFromToken(), commentId));
    }
}
