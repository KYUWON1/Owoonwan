package com.example.owoonwan.service;

import com.example.owoonwan.domain.Post;
import com.example.owoonwan.domain.PostComment;
import com.example.owoonwan.dto.dto.CreateCommentDto;
import com.example.owoonwan.dto.dto.DeletePostCommentDto;
import com.example.owoonwan.dto.dto.GetPostCommentDto;
import com.example.owoonwan.dto.response.CreateComment;
import com.example.owoonwan.exception.CommentException;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.exception.VerifyException;
import com.example.owoonwan.repository.jpa.PostCommentRepository;
import com.example.owoonwan.repository.jpa.PostRepository;
import com.example.owoonwan.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CreateCommentDto createComment(CreateComment.Request request) {
        if(!postRepository.existsById(request.getPostId())){
            throw new CommentException(ErrorCode.POST_NOT_FOUND);
        }
        PostComment comment = new PostComment();
        comment.setUserId(request.getUserId());
        comment.setPostId(request.getPostId());
        comment.setContent(request.getContent());
        PostComment save = postCommentRepository.save(comment);

        return CreateCommentDto.fromDomain(save);
    }

    @Transactional
    public List<GetPostCommentDto> getPostComment(Long postId,
                                                  Pageable pageable){
        if(!postRepository.existsById(postId)){
            throw new CommentException(ErrorCode.POST_NOT_FOUND);
        }
        Page<PostComment> postComments = postCommentRepository.findByPostId(postId, pageable);
        return postComments.stream()
                .map(GetPostCommentDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeletePostCommentDto deletePostComment(Long postId, String userId,
                                                  Long commentId) {
        // 게시글 작성자는 모든 댓글에 대해 삭제가능
        // 댓글 작성자는 자신의 댓글에 대해서만 삭제가능
        if(!CheckDBAndValidation(postId, commentId, userId)){
            throw new VerifyException(ErrorCode.NO_COMMENT_DELETE_PERMISSION);
        }
        postCommentRepository.deleteById(commentId);

        return new DeletePostCommentDto(commentId);
    }

    private boolean CheckDBAndValidation(Long postId,Long commentId,
                                         String userId){
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_EXIST));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
        if(post.getUserId().equals(userId)){
            return true;
        }
        if(comment.getUserId().equals(userId)){
            return true;
        }
        return false;
    }
}
