package com.example.owoonwan.controller;

import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.dto.CreateCommentDto;
import com.example.owoonwan.dto.dto.GetPostCommentDto;
import com.example.owoonwan.dto.response.CreateComment;
import com.example.owoonwan.dto.response.DeletePostCommentResponse;
import com.example.owoonwan.dto.response.GetCommentResponse;
import com.example.owoonwan.service.CommentService;
import com.example.owoonwan.utils.UserIdHolder;
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
@RequestMapping ("/api/v1/posts/{postId}/comments")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CreateComment.Response createComments(
            @PathVariable Long postId,
            @RequestBody CreateComment.Request request
    ){
        request.setUserId(UserIdHolder.getUserIdFromToken());
        request.setPostId(postId);
        return CreateComment.Response.from(commentService.createComment(request));
    }

    @GetMapping
    public List<GetCommentResponse> getPostComments(
            @PathVariable Long postId,
            @PageableDefault(size = 10, sort = "createdAt", direction =
                    Sort.Direction.DESC) Pageable pageable
    ){
        return commentService.getPostComment(postId, pageable).stream()
                .map(GetCommentResponse::from)
                .collect(Collectors.toList());
    }

    @DeleteMapping
    public DeletePostCommentResponse deletePostComments(
            @PathVariable Long postId,
            @RequestParam Long commentId
    ){
        return DeletePostCommentResponse
                .from(commentService.deletePostComment(postId,UserIdHolder.getUserIdFromToken(),commentId));
    }

}
