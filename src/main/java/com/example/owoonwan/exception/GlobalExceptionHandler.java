package com.example.owoonwan.exception;

import com.example.owoonwan.dto.response.ErrorResponse;
import com.example.owoonwan.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RedissonException.class)
    public ErrorResponse handleRedissonException(RedissonException e){
        log.error("RedissonException exception {} is occurred.");
        return new ErrorResponse(e.getErrorCode(),e.getDescription());
    }

    @ExceptionHandler(WorkOutGroupException.class)
    public ErrorResponse handleWorkOutGroupException(WorkOutGroupException e){
        log.error("WorkOutGroup exception {} is occurred.");
        return new ErrorResponse(e.getErrorCode(),e.getDescription());
    }

    @ExceptionHandler(CommentException.class)
    public ErrorResponse handleCommentException(CommentException e){
        log.error("Comment exception {} is occurred.");
        return new ErrorResponse(e.getErrorCode(),e.getDescription());
    }

    @ExceptionHandler(MediaException.class)
    public ErrorResponse handleMediaException(MediaException e){
        log.error("Media exception {} is occurred.");
        return new ErrorResponse(e.getErrorCode(),e.getDescription());
    }

    @ExceptionHandler(PostException.class)
    public ErrorResponse handlePostException(PostException e){
        log.error("Post exception {} is occurred.");
        return new ErrorResponse(e.getErrorCode(),e.getDescription());
    }

    @ExceptionHandler(UserException.class)
    public ErrorResponse handleUserException(UserException e){
        log.error("User exception {} is occurred.");
        return new ErrorResponse(e.getErrorCode(),e.getDescription());
    }

    @ExceptionHandler(VerifyException.class)
    public ErrorResponse handleVerifyException(VerifyException e){
        log.error("Verify exception {} is occurred.",e.getDescription());
        return new ErrorResponse(e.getErrorCode(),e.getDescription());
    }
    // 최종 예외처리
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e){
        log.error("Exception is occurred.",e);

        return new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR.getDescription()
        );
    }


}
