package com.example.owoonwan.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_ID_EXIST("해당 아이디는 이미 존재합니다."),
    NICKNAME_EXIST("해당 닉네임은 이미 존재합니다."),
    VERIFY_CODE_NOT_MATCH("인증번호가 옳바르지않습니다.");

    private final String description;
}
