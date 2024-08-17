package com.example.owoonwan.controller;

import com.example.owoonwan.config.SecurityConfig;
import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.UserInfoDto;
import com.example.owoonwan.dto.UserJoinDto;
import com.example.owoonwan.dto.response.DeleteUser;
import com.example.owoonwan.dto.response.UpdateUserIdAndNickName;
import com.example.owoonwan.dto.response.UpdateUserPassword;
import com.example.owoonwan.dto.response.UserJoin;
import com.example.owoonwan.jwt.JwtUtil;
import com.example.owoonwan.service.SmsVerificationService;
import com.example.owoonwan.service.UserService;
import com.example.owoonwan.type.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.HttpSecurityDsl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(JwtUtil.class)
class UserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private SmsVerificationService smsVerificationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Authentication 객체를 생성하고 SecurityContext에 설정
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("testId", null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Success createUser")
    void successCreateUser() throws Exception {
        // Given
        UserJoin.Request request = new UserJoin.Request();
        request.setUserId("test");
        request.setPassword("1234");
        request.setNickName("test");
        request.setPhoneNumber("01012341234");

        UserJoinDto userJoinDto = UserJoinDto.builder()
                .userId("testId")
                .nickName("testNickName")
                .build();

        given(userService.createUser(any(), any()))
                .willReturn(userJoinDto);

        MockHttpSession session = new MockHttpSession();
        doNothing().when(smsVerificationService).sendVerificationCode(any(String.class));

        // When & Then
        mockMvc.perform(post("/user/submit-form")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(session)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testId"))
                .andExpect(jsonPath("$.nickName").value("testNickName"));
    }

    @Test
    @DisplayName("Success getUserInfo")
    void successGetUserInfo() throws Exception {
        // Given
        String userId = "testId";
        String role = "user";
        Long expiredMs = 1000L;

        UserInfoDto user = UserInfoDto.builder()
                .userId(userId)
                .nickName("nickname")
                .phoneNumber("01012341234")
                .build();

        String token = jwtUtil.createJwtToken(userId, role, expiredMs);

        given(userService.getUserInfo(userId))
                .willReturn(user);

        // When & Then
        mockMvc.perform(get("/user/{userId}",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+ token)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.nickName").value("nickname"));
    }

    @Test
    @DisplayName("Failure getUserInfo - 토큰 유저 불일치")
    void failureGetUserInfoByUnMatchUserIdAndToken() throws Exception {
        // Given
        String userId = "wrongId";
        String wrongId = "1234";
        String role = "user";
        Long expiredMs = 1000L;

        // 문자열 사용 anyString x
        String token = jwtUtil.createJwtToken(wrongId, role, expiredMs);

        // When & Then

        // 문자열로 비교, enum타입으로 하면 jsonpath에러
        mockMvc.perform(get("/user/{userId}",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+ token)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("USER_INFO_UN_MATCH"));
    }

    @Test
    @DisplayName("Success deleteUser")
    void failureDeleteUser() throws Exception {
        // Given
        String userId = "testId";
        String role = "user";
        Long expiredMs = 1000L;

        DeleteUser user = DeleteUser.builder()
                .userId(userId)
                .build();

        String token = jwtUtil.createJwtToken(userId, role, expiredMs);

        given(userService.deleteUser(userId))
                .willReturn(user);

        // When & Then
        mockMvc.perform(delete("/user/{userId}",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+ token)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    @DisplayName("Success updateUserInfo")
    void successUpdateUserInfo() throws Exception {
        // Given
        String userId = "testId";
        String afterUserId = "afterI";
        String afterNickName = "afterN";
        String role = "user";
        Long expiredMs = 1000L;

        UpdateUserIdAndNickName.Request request = UpdateUserIdAndNickName.Request
                .builder()
                .userId("test")
                .nickName("test")
                .build();
        UpdateUserIdAndNickName.Response response = UpdateUserIdAndNickName.Response
                .builder()
                .userId(afterUserId)
                .nickName(afterNickName)
                .build();

        String token = jwtUtil.createJwtToken(userId, role, expiredMs);

        given(userService.updateUserIdAndNickName(anyString(),
                any(UpdateUserIdAndNickName.Request.class)))
                .willReturn(response);

        // When & Then
        mockMvc.perform(patch("/user/{userId}/info",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization","Bearer "+ token)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(afterUserId))
                .andExpect(jsonPath("$.nickName").value(afterNickName));
    }

    @Test
    @DisplayName("Success updateUserPassword")
    void successUpdateUserPassword() throws Exception {
        // Given
        String userId = "testId";
        String passwordBefore = "1234";
        String passwordAfter = "5678";
        String passwordDoubleCheck = "5678";
        String role = "user";
        Long expiredMs = 1000L;

        UpdateUserPassword.Request request = UpdateUserPassword.Request
                .builder()
                .passwordBefore(passwordBefore)
                .passwordAfter(passwordAfter)
                .passwordDoubleCheck(passwordDoubleCheck)
                .build();
        UpdateUserPassword.Response response =
                UpdateUserPassword.Response
                .builder()
                .userId(userId)
                .build();

        String token = jwtUtil.createJwtToken(userId, role, expiredMs);

        given(userService.updateUserPassword(anyString(),
                any(UpdateUserPassword.Request.class)))
                .willReturn(response);

        // When & Then
        mockMvc.perform(patch("/user/{userId}/password",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization","Bearer "+ token)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId));
    }
}
