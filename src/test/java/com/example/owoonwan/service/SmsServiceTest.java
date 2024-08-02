package com.example.owoonwan.service;

import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.service.DefaultMessageService;
import net.nurigo.sdk.message.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private DefaultMessageService messageService;

    @InjectMocks
    private SmsService smsService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(smsService, "messageService", messageService);
    }

    @Test
    @DisplayName("Success sendSms")
    void successSendSms() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        //given
        String toNum = "01012341234";
        String senderNum = "01012341234";
        String certCode = "Code";
        Message message = new Message();
        message.setFrom(senderNum);
        message.setTo(toNum);
        message.setText(certCode);

        //when
        smsService.sendSms(toNum,certCode);

        //then
        verify(messageService,times(1)).send(any(Message.class));
    }

    @Test
    @DisplayName("Failure sendSms - " +
            "NurigoMessageNotReceivedException")
    void failureSendSmsWithNurigoMessageNotReceivedException() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        // given
        String phoneNumber = "01012345678";
        String certificationCode = "123456";
        doThrow(new NurigoMessageNotReceivedException("Message not received"))
                .when(messageService).send(any(Message.class));

        // when
        smsService.sendSms(phoneNumber, certificationCode);

        // then
        verify(messageService, times(1)).send(any(Message.class));
    }

    @Test
    @DisplayName("Failure sendSms - generic Exception")
    void failureSendSmsWithGenericException() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        // given
        String phoneNumber = "01012345678";
        String certificationCode = "123456";
        doThrow(new RuntimeException("Generic exception"))
                .when(messageService).send(any(Message.class));

        // when
        smsService.sendSms(phoneNumber, certificationCode);

        // then
        verify(messageService, times(1)).send(any(Message.class));
    }
}