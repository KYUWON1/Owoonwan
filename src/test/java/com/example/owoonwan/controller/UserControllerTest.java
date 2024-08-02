package com.example.owoonwan.controller;

import com.example.owoonwan.config.SecurityConfig;
import com.example.owoonwan.dto.UserJoinDto;
import com.example.owoonwan.dto.response.UserJoin;
import com.example.owoonwan.service.SmsVerificationService;
import com.example.owoonwan.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.config.annotation.web.HttpSecurityDsl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private SmsVerificationService smsVerificationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Success - createUser")
    @WithMockUser
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
}
