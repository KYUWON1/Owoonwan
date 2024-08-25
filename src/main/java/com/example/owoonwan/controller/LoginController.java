package com.example.owoonwan.controller;

import com.example.owoonwan.dto.dto.UserDetailsDto;
import com.example.owoonwan.dto.response.ErrorResponse;
import com.example.owoonwan.dto.response.UserLogin;
import com.example.owoonwan.jwt.JwtUtil;
import com.example.owoonwan.type.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/api/v1/user/login")
    @Operation(summary = "로그인", description = "사용자가 로그인하여 JWT 토큰을 발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation =
                            UserLogin.Response.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void loginDocumentation(
            @Parameter(description = "로그인 정보 객체", required = true) @RequestBody UserLogin.Request request
    ) {
        // This method is only for Swagger documentation and does not actually perform login
    }

    @GetMapping("/user/me")
    @Operation(summary = "현재 사용자 정보 조회", description = "인증된 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getUserDetails(
            @Parameter(description = "현재 인증된 사용자 정보") Authentication authentication) {
        try {
            UserDetailsDto customUserDetails = (UserDetailsDto) authentication.getPrincipal();
            String userId = customUserDetails.getUsername();
            String role = customUserDetails.getAuthorities().iterator().next().getAuthority();
            return ResponseEntity.ok(UserLogin.Response.builder()
                    .success("Role: " + role)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, "사용자 정보를 가져올 수 없습니다.")
            );
        }
    }
}
