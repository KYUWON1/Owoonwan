package com.example.owoonwan.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_ID_EXIST("해당 아이디는 이미 존재합니다."),
    NICKNAME_EXIST("해당 닉네임은 이미 존재합니다."),
    VERIFY_CODE_NOT_MATCH("인증번호가 옳바르지않습니다."),
    INTERNAL_SERVER_ERROR("서버 내부의 오류입니다."),
    JWT_TOKEN_EXPIRED("토큰이 만료되었습니다."),
    JWT_TOKEN_NOT_CORRECT("토큰이 옳바르지않습니다.."),
    USER_NOT_FOUND("해당 계정은 존재하지 않습니다."),
    TOKEN_CREATE_ERROR("토큰 발급에 실패했습니다."),
    USER_INFO_UN_MATCH("유저정보가 일치하지 않습니다."),
    MAX_POST_SIZE_10("최대 10개의 파일만 등록 가능합니다."),
    PASSWORD_UN_MATCH("비밀번호가 일치하지 않습니다."),
    FILE_EXTENSION_NOT_EXIST("확장자명이 존재하지 않습니다."),
    FILE_EXTENSION_UNKNOWN("지원하지않는 파일 형식입니다."),
    FILE_IS_EMPTY("파일이 비어있습니다. 요청에서 제거해주세요"),
    S3_PUT_EXCEPTION("파일을 저장하는중 에러가 발생했습니다."),
    PASSWORD_DOUBLE_CHECK_UN_MATCH("확인 비밀번호가 일치하지 않습니다."),
    POST_NOT_FOUND("해당 게시물은 존재하지않습니다."),
    ZERO_POST("게시글이 존재하지 않습니다.");


    private final String description;
}
