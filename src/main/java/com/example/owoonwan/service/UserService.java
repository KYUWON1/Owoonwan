package com.example.owoonwan.service;

import com.example.owoonwan.dto.UserJoinDto;
import com.example.owoonwan.exception.UserException;
import com.example.owoonwan.repository.UserRepository;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.type.UserStatus;
import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.UserJoin;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    // BCrypt는 Config에서 따로 빈 등록후 사용 가능
    private final PasswordEncoder passwordEncoder;
    private final SmsVerificationService smsVerificationService;

    @Transactional
    public UserJoinDto createUser(
            UserJoin.Request request
            , HttpSession session) {
        // 아이디 닉네임 중복 체크
        checkForDuplicateUserIdOrNickName(request.getUserId(), request.getNickName());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .userId(request.getUserId())
                .password(encodedPassword)
                .nickName(request.getNickName())
                .phoneNumber(request.getPhoneNumber())
                .status(UserStatus.ACTIVE)
                .build();
        session.setAttribute("signUpRequest", user);
        session.setAttribute("phoneNumber", user.getPhoneNumber());
        smsVerificationService.sendVerificationCode(request.getPhoneNumber());

        return UserJoinDto.fromEntity(user);
    }

    private void checkForDuplicateUserIdOrNickName(String userId, String nickName) {
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new UserException(ErrorCode.USER_ID_EXIST);
        }
        if (userRepository.findByNickName(nickName).isPresent()) {
            throw new UserException(ErrorCode.NICKNAME_EXIST);
        }
    }
}
