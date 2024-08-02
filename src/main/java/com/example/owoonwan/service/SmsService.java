package com.example.owoonwan.service;


import io.github.cdimascio.dotenv.Dotenv;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;


@Service
public class SmsService {
    private final String apiKey;
    private final String apiSecret;
    private final String senderNumber;
    private final String smsUrl = "https://api.coolsms.co.kr";
    private final DefaultMessageService messageService;

    public SmsService() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("SMS_API_KEY");
        this.apiSecret = dotenv.get("SMS_API_SECRET");
        this.senderNumber = dotenv.get("SMS_PHONE_NUMBER");
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, smsUrl);
    }

    public void sendSms(String phoneNumber, String certificationCode) {
        // Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요
        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(phoneNumber);
        message.setText("(오운완) 휴대폰 인증번호: "+certificationCode);

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
