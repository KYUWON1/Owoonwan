package com.example.owoonwan.controller;

import com.example.owoonwan.dto.SmsVerification;
import com.example.owoonwan.dto.UserJoin;
import com.example.owoonwan.service.SmsService;
import com.example.owoonwan.service.SmsVerificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.owoonwan.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SmsVerificationService smsVerificationService;

    /**
     * 회원가입
     * 1. 사용자 아이디 및 닉네임 체크 후 인증번호 발송
     * 2. 인증성공시 데이터베이스 저장
     */
    @PostMapping("/user/submit")
    public UserJoin.Response submitUserForm(
            HttpSession session,
           @RequestBody @Valid UserJoin.Request request
    ){
        return UserJoin.Response.from(
                userService.createUser(request,session)
                ,"successfully send verifyCode.");
    }

    @PostMapping("/user/register")
    public SmsVerification.Response verifyAndSaveUser(
            HttpSession session,
            @RequestBody @Valid SmsVerification.Request request
    ){
        return SmsVerification.Response.from(
                smsVerificationService.verifyCode(session,request.getVerifyCode())
        ,"successfully save User");
    }
}
