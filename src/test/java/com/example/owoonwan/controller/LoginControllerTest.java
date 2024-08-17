package com.example.owoonwan.controller;

import com.example.owoonwan.annotation.WithMockCustomUser;
import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.response.UserLogin;
import com.example.owoonwan.jwt.JwtUtil;
import com.example.owoonwan.type.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Success - login")
    @WithMockCustomUser(username = "test",password = "1234")
    void successLogin() throws Exception {
        //given
        UserLogin.Request request = new UserLogin.Request("test","1234");

        Authentication atc = new TestingAuthenticationToken("test",null,"USER");

        User user = User.builder()
                .userId("test")
                .password("1234")
                .nickName("testNick")
                .phoneNumber("01012341234")
                .role(UserRole.USER)
                .build();

        String token = "mocked-token";

        given(authenticationManager.authenticate(any()))
                .willReturn(atc);
        given(jwtUtil.createJwtToken("test",UserRole.USER.toString(),3600000L))
                .willReturn(token);
        //when
        //then
        ResultActions actions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token")
                .with(csrf())
                .with(authentication(atc)));
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("test"))
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.description").value("토큰이 성공적으로 발급되었습니다" +
                        "."));

    }


}