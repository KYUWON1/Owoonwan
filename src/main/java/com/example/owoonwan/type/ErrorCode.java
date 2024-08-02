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
    USER_NOT_FOUND("해당 계정은 존재하지 않습니다."),
    TOKEN_CREATE_ERROR("토큰 발급에 실패했습니다.");
    private final String description;
}
