package com.example.owoonwan.service;

import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.SmsVerificationDto;
import com.example.owoonwan.exception.VerifyException;
import com.example.owoonwan.repository.UserRepository;
import com.example.owoonwan.type.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoSession;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsVerificationServiceTest {
    @Mock
    private SmsService smsService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SmsVerificationService smsVerificationService;

    private Map<String,String> phoneCodeMap;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        phoneCodeMap = new HashMap<>();
        session = new MockHttpSession();
        ReflectionTestUtils.setField(smsVerificationService, "phoneCodeMap", phoneCodeMap);

        ReflectionTestUtils.setField(smsVerificationService,"userRepository",
                userRepository);
        ReflectionTestUtils.setField(smsVerificationService,"smsService",
                smsService);
    }

    @Test
    @DisplayName("Success sendVerificationCode")
    void successSendVerificationCode() {
        //given
        String phoneNumber = "01012341234";

        //when
        smsVerificationService.sendVerificationCode(phoneNumber);

        //then
        verify(smsService,times(1)).sendSms(anyString(),anyString());
        assertNotNull(phoneCodeMap.get(phoneNumber)); // Verify that the code is stored in the map
    }

    @Test
    @DisplayName("Success verifyCode")
    void successVerifyCode() {
        //given
        String certificateCode = "123456";
        String phoneNumber= "01012341234";
        User user = new User();
        user.setUserId("test");

        phoneCodeMap.put(phoneNumber,certificateCode);
        session.setAttribute("signUpRequest",user);
        session.setAttribute("phoneNumber",phoneNumber);

        //when
        SmsVerificationDto result = smsVerificationService.verifyCode(session,
                certificateCode);

        //then
        verify(userRepository,times(1)).save(user);
        assertEquals(result.getUserId(),"test");
        assertNull(phoneCodeMap.get(phoneNumber));
    }

    @Test
    @DisplayName("Failure verifyCode - 인증 코드 에러")
    void failVerifyCode() {
        //given
        String certificateCode = "123456";
        String phoneNumber= "01012341234";
        String wrongCode = "123123";

        phoneCodeMap.put(phoneNumber,certificateCode);
        session.setAttribute("phoneNumber",phoneNumber);

        //when
        VerifyException exception = assertThrows(VerifyException.class, () ->
                smsVerificationService.verifyCode(session, wrongCode));

        //then
        verify(userRepository,never()).save(any());
        assertEquals(exception.getErrorCode(), ErrorCode.VERIFY_CODE_NOT_MATCH);
    }

}