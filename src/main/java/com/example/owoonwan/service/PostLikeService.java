package com.example.owoonwan.service;

import com.example.owoonwan.domain.PostLike;
import com.example.owoonwan.dto.dto.CreateLikeDto;
import com.example.owoonwan.dto.dto.GetPostLikeInfoDto;
import com.example.owoonwan.exception.PostException;
import com.example.owoonwan.repository.jpa.PostLikeRepository;
import com.example.owoonwan.repository.jpa.PostRepository;
import com.example.owoonwan.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public CreateLikeDto createLike(Long postId, String userId) {
        // 게시글이 존재하는지 확인
        if(!postRepository.existsById(postId)){
            throw new PostException(ErrorCode.POST_NOT_FOUND);
        }
        Optional<PostLike> like = postLikeRepository.findByPostIdAndUserId(postId, userId);
        // 이미 좋아요를 눌렀다면 -> 삭제, 좋아요가 없다면 생성
        if(like.isPresent()){
            postLikeRepository.delete(like.get());
            return CreateLikeDto.fromDomainUnDoLike(like.get());
        }else{
            PostLike likeNew = new PostLike();
            likeNew.setUserId(userId);
            likeNew.setPostId(postId);
            return CreateLikeDto.fromDomainDoLike(postLikeRepository.save(likeNew));
        }
    }

    @Transactional
    public Long getLikeCount(Long postId) {
        // 게시글이 존재하는지 확인
        if(!postRepository.existsById(postId)){
            throw new PostException(ErrorCode.POST_NOT_FOUND);
        }
        return postLikeRepository.countByPostId(postId);
    }

    @Transactional
    public List<GetPostLikeInfoDto> getPostLikeInfo(Long postId) {
        // 게시글이 존재하는지 확인
        if(!postRepository.existsById(postId)){
            throw new PostException(ErrorCode.POST_NOT_FOUND);
        }
        List<PostLike> allLikes = postLikeRepository.findAllByPostId(postId);
        return allLikes.stream()
                .map(GetPostLikeInfoDto::fromDomain)
                .collect(Collectors.toList());
    }
}
