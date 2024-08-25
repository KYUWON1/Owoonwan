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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;
    private final SmsVerificationService smsVerificationService;

    @PostMapping("/submit-form")
    @Operation(summary = "사용자 가입 신청", description = "사용자 정보를 받아 가입 신청을 처리하고, " +
            "SMS 인증번호를 발송합니다.")
    public UserJoin.Response submitUserForm(
            HttpSession session,
            @RequestBody @Valid @Parameter(description = "사용자 가입 요청 정보") UserJoin.Request request
    ){
        return UserJoin.Response.from(
                userService.createUser(request,session)
                ,"successfully send verifyCode.");
    }

    @PostMapping("/register")
    @Operation(summary = "SMS 인증 및 사용자 등록", description = "SMS 인증 코드를 검증하고 사용자를 등록합니다.")
    public SmsVerification.Response verifyAndSaveUser(
            HttpSession session,
            @RequestBody @Valid @Parameter(description = "SMS 인증 코드") SmsVerification.Request request
    ){
        return SmsVerification.Response.from(
                smsVerificationService.verifyCode(session,request.getVerifyCode())
                ,"successfully save User");
    }

    @GetMapping("/{userId}")
    @Operation(summary = "사용자 정보 조회", description = "특정 사용자의 정보를 조회합니다.")
    public UserInfoDto getUserInfo(
            @Parameter(description = "사용자 ID") @PathVariable String userId
    ) {
        String tokenId =
                SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userId.equals(tokenId)){
            throw new VerifyException(ErrorCode.USER_INFO_UN_MATCH);
        }
        return userService.getUserInfo(userId);
    }

    @PatchMapping("/{userId}/info")
    @Operation(summary = "사용자 정보 수정", description = "사용자의 ID와 닉네임을 수정합니다.")
    public UpdateUserIdAndNickName.Response updateUser(
            @Parameter(description = "사용자 ID") @PathVariable String userId,
            @RequestBody @Parameter(description = "수정할 사용자 ID와 닉네임 요청 정보") UpdateUserIdAndNickName.Request request
    ){
        return userService.updateUserIdAndNickName(userId,request);
    }

    @PatchMapping("/{userId}/password")
    @Operation(summary = "사용자 비밀번호 수정", description = "현재 비밀번호를 확인하고, 사용자의 " +
            "비밀번호를 수정합니다.")
    public UpdateUserPassword.Response updateUser(
            @Parameter(description = "사용자 ID") @PathVariable String userId,
            @RequestBody @Parameter(description = "기존 비밀번호 및 수정할 비밀번호 요청 정보") UpdateUserPassword.Request request
    ){
        return userService.updateUserPassword(userId,request);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "사용자 삭제", description = "특정 사용자를 삭제합니다.")
    public DeleteUser deleteUser(
            @Parameter(description = "사용자 ID") @PathVariable String userId
    ) {
        return userService.deleteUser(userId);
    }

}
