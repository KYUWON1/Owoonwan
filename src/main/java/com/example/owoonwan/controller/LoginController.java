package com.example.owoonwan.controller;

import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.UserDetailsDto;
import com.example.owoonwan.dto.response.ErrorResponse;
import com.example.owoonwan.dto.response.UserLogin;
import com.example.owoonwan.jwt.JwtUtil;
import com.example.owoonwan.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Long expiredMs = 10 * 60 * 60 * 1000L;

//    // 에러 객체를 전달해야 에러가 발생하지않음
//    @PostMapping("/login")
//    public ResponseEntity<?> login(
//            @RequestBody UserLogin.Request request
//    ) {
//        try{
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getUserId(),request.getPassword())
//            );
//
//            User customUserDetails =
//                    (User) authentication.getPrincipal();
//            String userId = customUserDetails.getUsername();
//            String role =
//                    customUserDetails.getAuthorities().iterator().next().getAuthority();
//            String token = jwtUtil.createJwtToken(userId,role,expiredMs);
//
//            return ResponseEntity.ok(UserLogin.Response.builder()
//                    .userId(userId)
//                    .token(token)
//                    .description("토큰이 성공적으로 발급되었습니다.")
//                    .build());
//        }catch (AuthenticationException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
//                    new ErrorResponse(ErrorCode.TOKEN_CREATE_ERROR, e.getMessage())
//            );
//        }
//    }

    @GetMapping("/user/me")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        try {
            UserDetailsDto customUserDetails = (UserDetailsDto) authentication.getPrincipal();
            String userId = customUserDetails.getUsername();
            String role = customUserDetails.getAuthorities().iterator().next().getAuthority();
            return ResponseEntity.ok(UserLogin.Response.builder()
                    .userId(userId)
                    .description("Role :"+ role)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, "사용자 정보를 가져올 수 없습니다.")
            );
        }
    }
}
