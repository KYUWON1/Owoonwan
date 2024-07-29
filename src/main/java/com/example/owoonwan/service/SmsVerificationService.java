package com.example.owoonwan.service;

import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.SmsVerificationDto;
import com.example.owoonwan.exception.VerifyException;
import com.example.owoonwan.repository.UserRepository;
import com.example.owoonwan.type.ErrorCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsVerificationService {
    private final Map<String,String > phoneCodeMap = new HashMap<>();
    private final SmsService smsService;
    private final UserRepository userRepository;

    public void sendVerificationCode(String phoneNumber){
        String CertificationCode = generateCode();
        phoneCodeMap.put(phoneNumber,CertificationCode);
        smsService.sendSms(phoneNumber,CertificationCode);
    }

    public SmsVerificationDto verifyCode(HttpSession session, String certificationCode){
        String phoneNumber = (String)session.getAttribute("phoneNumber");
        String storedCode = phoneCodeMap.get(phoneNumber);
        System.out.println(storedCode+":"+certificationCode);
        if(storedCode == null || !storedCode.equals(certificationCode)){
            throw new VerifyException(ErrorCode.VERIFY_CODE_DISMATCH);
        }
        phoneCodeMap.remove(certificationCode);
        User user = (User)session.getAttribute("signUpRequest");
        userRepository.save(user);
        return new SmsVerificationDto(user.getUserId());
    }

    private String generateCode(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리인증번호
        return String.valueOf(code);
    }
}
