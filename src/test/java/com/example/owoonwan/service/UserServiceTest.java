package com.example.owoonwan.service;

import com.example.owoonwan.config.SecurityConfig;
import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.UserInfoDto;
import com.example.owoonwan.dto.UserJoinDto;
import com.example.owoonwan.dto.response.UserJoin;
import com.example.owoonwan.exception.UserException;
import com.example.owoonwan.repository.UserRepository;
import com.example.owoonwan.type.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private SmsVerificationService smsVerificationService;

    @InjectMocks
    private UserService userService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        session = new MockHttpSession();
        // password encoder 에러 해결, 프라이빗 필드 접근 가능
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
        ReflectionTestUtils.setField(userService, "userRepository",
                userRepository);
    }

    @Test
    @DisplayName("Success createUser")
    void successCreateUser() {
        //given
        UserJoin.Request request =
                new UserJoin.Request("userId", "12341234", "nickname",
                        "01012345678");
        given(passwordEncoder.encode(anyString())).willReturn("encodePassword");

        //when
        UserJoinDto user = userService.createUser(request, session);
        User sessionUser = (User) session.getAttribute("signUpRequest");

        //then
        assertEquals("userId",user.getUserId());
        assertEquals("nickname",user.getNickName());
        assertEquals("01012345678",session.getAttribute("phoneNumber"));
        assertEquals("encodePassword",sessionUser.getPassword());
    }

    @Test
    @DisplayName("Failure createUser - 해당 ID 존재")
    void failCreateUserForIdDup() {
        //given
        UserJoin.Request request =
                new UserJoin.Request("userId", "12341234", "nickname",
                        "01012345678");
        given(userRepository.findByUserId(anyString()))
                .willReturn(Optional.of(new User()));
        //when
        UserException exception = assertThrows(UserException.class,
                () -> userService.createUser(request, session));
        //then
        assertEquals(ErrorCode.USER_ID_EXIST,exception.getErrorCode());
    }

    @Test
    @DisplayName("Failure createUser - 해당 닉네임 존재")
    void failCreateUserForNickNameDup() {
        //given
        UserJoin.Request request =
                new UserJoin.Request("userId", "12341234", "nickname",
                        "01012345678");
        given(userRepository.findByNickName(anyString()))
                .willReturn(Optional.of(new User()));
        //when
        UserException exception = assertThrows(UserException.class,
                () -> userService.createUser(request, session));
        //then
        assertEquals(ErrorCode.NICKNAME_EXIST,exception.getErrorCode());
    }

    @Test
    @DisplayName("Success getUserInfo")
    void successGetUserInfo() {
        //given
        User user = User.builder()
                .userId("testId")
                .nickName("testNick")
                .phoneNumber("01012341234")
                .createdAt(Date.valueOf(LocalDate.now()))
                .build();
        given(userRepository.findByUserId(anyString()))
                .willReturn(Optional.of(user));
        //when
        UserInfoDto result = userService.getUserInfo("testId");

        //then
        assertEquals(result.getUserId(),"testId");
        assertEquals(result.getNickName(),"testNick");
        assertEquals(result.getPhoneNumber(),"01012341234");
    }

    @Test
    @DisplayName("Failure getUserInfo - 유저 발견 못함")
    void failureGetUserInfoByUserNotFound() {
        //given
        given(userRepository.findByUserId(anyString()))
                .willReturn(Optional.empty());
        //when
        UserException exception = assertThrows(UserException.class,
                () -> userService.getUserInfo(anyString()));

        //then
        assertEquals(exception.getErrorCode(),ErrorCode.USER_NOT_FOUND);
    }

}