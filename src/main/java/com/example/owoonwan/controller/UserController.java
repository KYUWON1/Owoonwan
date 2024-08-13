package com.example.owoonwan.controller;

import com.example.owoonwan.dto.response.*;
import com.example.owoonwan.dto.dto.UserInfoDto;
import com.example.owoonwan.exception.VerifyException;
import com.example.owoonwan.service.SmsVerificationService;
import com.example.owoonwan.type.ErrorCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.owoonwan.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SmsVerificationService smsVerificationService;

    @PostMapping("/user/submit-form")
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

    @GetMapping("/user/{userId}")
    public UserInfoDto getUserInfo(
            @PathVariable String userId
    ) {
        String tokenId =
                SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userId.equals(tokenId)){
            throw new VerifyException(ErrorCode.USER_INFO_UN_MATCH);
        }
        return userService.getUserInfo(userId);
    }

    // deleteAt 컬럼 제거하기
    @DeleteMapping("/user/{userId}")
    public DeleteUser deleteUser(
            @PathVariable String userId
    ) {
        return userService.deleteUser(userId);
    }

    @PatchMapping("/user/{userId}/info")
    public UpdateUserIdAndNickName.Response updateUser(
        @PathVariable String userId,
        @RequestBody UpdateUserIdAndNickName.Request request
    ){
        return userService.updateUserIdAndNickName(userId,request);
    }

    @PatchMapping("/user/{userId}/password")
    public UpdateUserPassword.Response updateUser(
            @PathVariable String userId,
            @RequestBody UpdateUserPassword.Request request
    ){
        return userService.updateUserPassword(userId,request);
    }

}
