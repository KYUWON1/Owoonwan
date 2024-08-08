package com.example.owoonwan;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OwoonwanApplication {
    public static void main(String[] args) {
        // yml에서 환경변수를 불러오려면, .env가 아닌, 시스템 환경변수에서
        // 불러오기 때문에, .env 변수를 시작전에 시스템에 등록
        Dotenv dotenv = Dotenv.load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(),entry.getValue()));

        SpringApplication.run(OwoonwanApplication.class, args);
    }

}
