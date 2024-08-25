package com.example.owoonwan.service;

import io.github.cdimascio.dotenv.Dotenv;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private final String apiKey;
    private final String apiSecret;
    private final String senderNumber;
    private final String smsUrl = "https://api.coolsms.co.kr";
    private final DefaultMessageService messageService;

    public SmsService(
            @Value("${SMS_API_KEY}") String apiKey,
            @Value("${SMS_API_SECRET}") String apiSecret,
            @Value("${SMS_PHONE_NUMBER}") String senderNumber
    ) {
        Dotenv dotenv = Dotenv.load();
        if(apiKey == null){
            apiKey = dotenv.get("SMS_API_KEY");
        }
        if(apiSecret == null){
            apiSecret = dotenv.get("SMS_API_SECRET");
        }
        if(senderNumber == null){
            senderNumber = dotenv.get("SMS_PHONE_NUMBER");
        }
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.senderNumber = senderNumber;
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, smsUrl);
    }

    public void sendSms(String phoneNumber, String certificationCode) {
        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(phoneNumber);
        message.setText("(오운완) 휴대폰 인증번호: " + certificationCode);

        try {
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
