package com.example.owoonwan.config;

import com.example.owoonwan.domain.User;
import com.example.owoonwan.repository.jpa.UserRepository;
import com.example.owoonwan.type.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String password = "1234";
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .userId("test")
                .password(encodedPassword)
                .nickName("nickName")
                .phoneNumber("01012341234")
                .role(UserRole.USER)
                .build();
        User user2 = User.builder()
                .userId("test2")
                .password(encodedPassword)
                .nickName("nickName2")
                .phoneNumber("01012341234")
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
        userRepository.save(user2);
    }
}
