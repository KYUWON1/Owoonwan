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
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
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
        User user = User.builder()
                .userId("test")
                .password("1234")
                .nickName("testNick")
                .phoneNumber("01012341234")
                .role(UserRole.USER)
                .build();

        String token = "mocked-token";
        Authentication auth =
                new UsernamePasswordAuthenticationToken(user,
                        null,user.getAuthorities());
        //((UsernamePasswordAuthenticationToken) auth).setAuthenticated(true);
        given(authenticationManager.authenticate(any()))
                .willReturn(auth);
        given(jwtUtil.createJwtToken("test",UserRole.USER.toString(),3600000L))
                .willReturn(token);
        //when
        //then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("test"))
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.description").value("토큰이 성공적으로 발급되었습니다" +
                        "."));
    }

    @Test
    @WithMockUser(username="user1", roles={"USER"}) // 인증된 사용자를 시뮬레이션
    public void testLoginSuccess() throws Exception {
        // 로그인 데이터 준비
        Map<String, String> credentials = new HashMap<>();
        credentials.put("userId", "user1");
        credentials.put("password", "password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(credentials);

        // 로그인 POST 요청
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk()) // 200 상태 코드 기대
                .andExpect(content().string(containsString("Authentication successful"))); // 응답 내용 검증
    }

}