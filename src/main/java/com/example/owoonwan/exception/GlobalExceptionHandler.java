package com.example.owoonwan.exception;

import com.example.owoonwan.dto.response.ErrorResponse;
import com.example.owoonwan.type.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExpiredJwtException> handleExpiredJwtException(ExpiredJwtException e) {
        log.error("ExpiredJwtException exception {} is occurred.", e.getMessage());
        return new ResponseEntity<>(e, HttpStatus.CONFLICT);  // 상태 코드 설정
    }

    @ExceptionHandler(RedissonException.class)
    public ResponseEntity<ErrorResponse> handleRedissonException(RedissonException e) {
        log.error("RedissonException exception {} is occurred.", e.getDescription());
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getDescription());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);  // 상태 코드 설정
    }

    @ExceptionHandler(WorkOutGroupException.class)
    public ResponseEntity<ErrorResponse> handleWorkOutGroupException(WorkOutGroupException e) {
        log.error("WorkOutGroup exception {} is occurred.", e.getDescription());
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getDescription());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  // 상태 코드 설정
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorResponse> handleCommentException(CommentException e) {
        log.error("Comment exception {} is occurred.", e.getDescription());
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getDescription());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  // 상태 코드 설정
    }

    @ExceptionHandler(MediaException.class)
    public ResponseEntity<ErrorResponse> handleMediaException(MediaException e) {
        log.error("Media exception {} is occurred.", e.getDescription());
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getDescription());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  // 상태 코드 설정
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> handlePostException(PostException e) {
        log.error("Post exception {} is occurred.", e.getDescription());
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getDescription());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  // 상태 코드 설정
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        log.error("User exception {} is occurred.", e.getDescription());
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getDescription());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  // 상태 코드 설정
    }

    @ExceptionHandler(VerifyException.class)
    public ResponseEntity<ErrorResponse> handleVerifyException(VerifyException e) {
        log.error("Verify exception {} is occurred.", e.getDescription());
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getDescription());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  // 상태 코드 설정
    }

    // 최종 예외처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception is occurred.", e);
        ErrorResponse response = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR.getDescription()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  // 상태 코드 설정
    }
}
