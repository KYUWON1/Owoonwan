package com.example.owoonwan.service;

import com.example.owoonwan.config.SecurityConfig;
import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.UserInfoDto;
import com.example.owoonwan.dto.UserJoinDto;
import com.example.owoonwan.dto.response.DeleteUser;
import com.example.owoonwan.dto.response.UpdateUserIdAndNickName;
import com.example.owoonwan.dto.response.UpdateUserPassword;
import com.example.owoonwan.dto.response.UserJoin;
import com.example.owoonwan.exception.UserException;
import com.example.owoonwan.exception.VerifyException;
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

    @Test
    @DisplayName("Success deleteUser")
    void successDeleteUser() {
        //given
        User user = User.builder()
                .userId("test")
                .build();
        given(userRepository.findByUserId(anyString()))
                .willReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        //when
        DeleteUser result = userService.deleteUser("testId");

        //then
        assertNotNull(result);
        assertEquals(result.getUserId(),user.getUserId());
        verify(userRepository,times(1)).delete(user);
    }

    @Test
    @DisplayName("Failure deleteUser - 유저 정보 없음")
    void failureDeleteUserByUserNotFound() {
        //given
        given(userRepository.findByUserId(anyString()))
                .willReturn(Optional.empty());

        //when
        UserException exception = assertThrows(UserException.class,
                () -> userService.deleteUser(anyString()));

        //then
        assertEquals(exception.getErrorCode(),ErrorCode.USER_NOT_FOUND);
        verify(userRepository,times(0)).delete(any());
    }

    @Test
    @DisplayName("Success updateUserIdAndNickName")
    void successUpdateUserIdAndNickName() {
        //given
        String userId = "test";
        String nickName = "nickName";
        String userIdAfter = "afterId";
        String nickNameAfter = "afterNick";
        User user = User.builder()
                .userId(userId)
                .nickName(nickName)
                .build();
        UpdateUserIdAndNickName.Request request =
                UpdateUserIdAndNickName.Request.builder()
                        .userId(userIdAfter)
                        .nickName(nickNameAfter)
                        .build();

        given(userRepository.findByUserId("test"))
                .willReturn(Optional.of(user));
        given(userRepository.findByUserId(userIdAfter))
                .willReturn(Optional.empty());
        given(userRepository.findByNickName(nickNameAfter))
                .willReturn(Optional.empty());

        //when
        UpdateUserIdAndNickName.Response response =
                userService.updateUserIdAndNickName(userId, request);

        //then
        assertEquals(response.getUserId(),userIdAfter);
        assertEquals(response.getNickName(),nickNameAfter);
    }

    @Test
    @DisplayName("Failure updateUserIdAndNickName - 해당 유저 없음")
    void failureUpdateUserIdAndNickNameByUserNotFound() {
        //given
        String userId = "test";
        UpdateUserIdAndNickName.Request request =
                UpdateUserIdAndNickName.Request.builder()
                        .build();

        given(userRepository.findByUserId(userId))
                .willReturn(Optional.empty());

        //when
        UserException exception = assertThrows(UserException.class,
                () -> userService.updateUserIdAndNickName(userId, request));

        //then
        assertEquals(exception.getErrorCode(),ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("Failure updateUserIdAndNickName - 아이디 중복")
    void failureUpdateUserIdAndNickNameByIdDup() {
        //given
        String userId = "test";
        String userIdAfter = "after";
        String nickNameAfter = "after";
        User user = User.builder()
                .userId(userId)
                .build();
        UpdateUserIdAndNickName.Request request =
                UpdateUserIdAndNickName.Request.builder()
                        .userId(userIdAfter)
                        .nickName(nickNameAfter)
                        .build();

        given(userRepository.findByUserId(userId))
                .willReturn(Optional.of(user));
        given(userRepository.findByUserId(userIdAfter))
                .willReturn(Optional.of(user));
        //when
        UserException exception = assertThrows(UserException.class,
                () -> userService.updateUserIdAndNickName(userId, request));

        //then
        assertEquals(exception.getErrorCode(),ErrorCode.USER_ID_EXIST);
    }

    @Test
    @DisplayName("Failure updateUserIdAndNickName - 닉네임 중복")
    void failureUpdateUserIdAndNickNameByNickDup() {
        //given
        String userId = "test";
        String userIdAfter = "after";
        String nickNameAfter = "after";
        User user = User.builder()
                .userId(userId)
                .build();
        UpdateUserIdAndNickName.Request request =
                UpdateUserIdAndNickName.Request.builder()
                        .userId(userIdAfter)
                        .nickName(nickNameAfter)
                        .build();

        given(userRepository.findByUserId(userId))
                .willReturn(Optional.of(user));
        given(userRepository.findByUserId(userIdAfter))
                .willReturn(Optional.empty());
        given(userRepository.findByNickName(nickNameAfter))
                .willReturn(Optional.of(user));
        //when
        UserException exception = assertThrows(UserException.class,
                () -> userService.updateUserIdAndNickName(userId, request));

        //then
        assertEquals(exception.getErrorCode(),ErrorCode.NICKNAME_EXIST);
    }

    @Test
    @DisplayName("Success updateUserPassword")
    void successUpdateUserPassword() {
        //given
        String userId = "test";
        String passwordBefore = "1234";
        String passwordAfter = "5678";
        String passwordDoubleCheck = "5678";

        String encodedPassword = passwordEncoder.encode(passwordBefore);

        User user = User.builder()
                .userId(userId)
                .password(encodedPassword)
                .build();

        UpdateUserPassword.Request request =
                UpdateUserPassword.Request.builder()
                        .passwordBefore(passwordBefore)
                        .passwordAfter(passwordAfter)
                        .passwordDoubleCheck(passwordDoubleCheck)
                        .build();

        given(userRepository.findByUserId(userId))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(passwordBefore, user.getPassword()))
                .willReturn(true);

        //when
        UpdateUserPassword.Response response =
                userService.updateUserPassword(userId, request);


        //then
        assertEquals(response.getUserId(),userId);
        verify(userRepository,times(1)).save(user);
    }

    @Test
    @DisplayName("Failure updateUserPassword - 해당 유저 없음")
    void failureUpdateUserPasswordByUserNotFound() {
        //given
        String userId = "test";
        String passwordBefore = "1234";
        String passwordAfter = "5678";
        String passwordDoubleCheck = "5678";

        User user = User.builder()
                .userId(userId)
                .password(passwordBefore)
                .build();

        UpdateUserPassword.Request request =
                UpdateUserPassword.Request.builder()
                        .passwordBefore(passwordBefore)
                        .passwordAfter(passwordAfter)
                        .passwordDoubleCheck(passwordDoubleCheck)
                        .build();

        given(userRepository.findByUserId(userId))
                .willReturn(Optional.empty());

        //when
        UserException exception = assertThrows(UserException.class,
                () -> userService.updateUserPassword(userId, request));


        //then
        assertEquals(exception.getErrorCode(),ErrorCode.USER_NOT_FOUND);
        verify(userRepository,times(0)).save(user);
    }

    @Test
    @DisplayName("Failure updateUserPassword - 비밀번호 불일치")
    void failureUpdateUserPasswordByPasswordUnMatch() {
        //given
        String userId = "test";
        String wrongPassword = "wrong";
        String passwordBefore = "1234";
        String passwordAfter = "5678";
        String passwordDoubleCheck = "5678";

        String encodedPassword = passwordEncoder.encode(passwordBefore);

        User user = User.builder()
                .userId(userId)
                .password(encodedPassword)
                .build();

        UpdateUserPassword.Request request =
                UpdateUserPassword.Request.builder()
                        .passwordBefore(wrongPassword)
                        .passwordAfter(passwordAfter)
                        .passwordDoubleCheck(passwordDoubleCheck)
                        .build();

        given(userRepository.findByUserId(userId))
                .willReturn(Optional.of(user));

        given(passwordEncoder.matches(wrongPassword, user.getPassword()))
                .willReturn(false);

        //when
        VerifyException exception = assertThrows(VerifyException.class,
                () -> userService.updateUserPassword(userId, request));


        //then
        assertEquals(exception.getErrorCode(),ErrorCode.PASSWORD_UN_MATCH);
        verify(userRepository,times(0)).save(user);
    }

    @Test
    @DisplayName("Failure updateUserPassword - 확인 비밀번호 불일치")
    void failureUpdateUserPasswordByPasswordDoubleCheckUnMatch() {
        //given
        String userId = "test";
        String passwordBefore = "1234";
        String passwordAfter = "5678";
        String passwordDoubleCheckWrong = "wrong";

        String encodedPassword = passwordEncoder.encode(passwordBefore);

        User user = User.builder()
                .userId(userId)
                .password(encodedPassword)
                .build();

        UpdateUserPassword.Request request =
                UpdateUserPassword.Request.builder()
                        .passwordBefore(passwordBefore)
                        .passwordAfter(passwordAfter)
                        .passwordDoubleCheck(passwordDoubleCheckWrong)
                        .build();

        given(userRepository.findByUserId(userId))
                .willReturn(Optional.of(user));

        given(passwordEncoder.matches(passwordBefore, user.getPassword()))
                .willReturn(true);

        //when
        VerifyException exception = assertThrows(VerifyException.class,
                () -> userService.updateUserPassword(userId, request));


        //then
        assertEquals(exception.getErrorCode(),ErrorCode.PASSWORD_DOUBLE_CHECK_UN_MATCH);
        verify(userRepository,times(0)).save(user);
    }
}