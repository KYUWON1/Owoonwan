package com.example.owoonwan.service;

import com.example.owoonwan.dto.dto.UserDetailsDto;
import com.example.owoonwan.dto.dto.UserInfoDto;
import com.example.owoonwan.dto.dto.UserJoinDto;
import com.example.owoonwan.dto.response.DeleteUser;
import com.example.owoonwan.dto.response.UpdateUserIdAndNickName;
import com.example.owoonwan.dto.response.UpdateUserPassword;
import com.example.owoonwan.exception.UserException;
import com.example.owoonwan.exception.VerifyException;
import com.example.owoonwan.repository.jpa.UserRepository;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.type.UserRole;
import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.response.UserJoin;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
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
                .role(UserRole.USER)
                .build();
        session.setAttribute("signUpRequest", user);
        session.setAttribute("phoneNumber", user.getPhoneNumber());
        smsVerificationService.sendVerificationCode(request.getPhoneNumber());

        return UserJoinDto.fromEntity(user);
    }

    @Transactional
    public UserInfoDto getUserInfo(String userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserException(ErrorCode.USER_NOT_FOUND));
        return UserInfoDto.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    public DeleteUser deleteUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);

        return DeleteUser.builder()
                .userId(user.getUserId())
                .deletedAt(LocalDate.now())
                .build();
    }

    @Transactional
    public UpdateUserIdAndNickName.Response updateUserIdAndNickName(String userId, UpdateUserIdAndNickName.Request request) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND));

        checkForDuplicateUserIdOrNickName(request.getUserId(), request.getNickName());

        user.setUserId(request.getUserId());
        user.setNickName(request.getNickName());

        return new UpdateUserIdAndNickName.Response(user.getUserId(),
                user.getNickName());
    }

    public UpdateUserPassword.Response updateUserPassword(String userId, UpdateUserPassword.Request request) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND));

        checkForUserPasswordAndDoubleCheck(user.getPassword(),request);
        String encodedPassword =
                passwordEncoder.encode(request.getPasswordAfter());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        return new UpdateUserPassword.Response(user.getUserId());
    }

    private void checkForDuplicateUserIdOrNickName(String userId, String nickName) {
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new UserException(ErrorCode.USER_ID_EXIST);
        }
        if (userRepository.findByNickName(nickName).isPresent()) {
            throw new UserException(ErrorCode.NICKNAME_EXIST);
        }
    }

    private void checkForUserPasswordAndDoubleCheck(
            String savedPassword,
            UpdateUserPassword.Request request
    ){
        if(!passwordEncoder.matches(
                request.getPasswordBefore(),savedPassword)){
            throw new VerifyException(ErrorCode.PASSWORD_UN_MATCH);
        }
        if(!request.getPasswordAfter().equals(request.getPasswordDoubleCheck())){
            throw new VerifyException(ErrorCode.PASSWORD_DOUBLE_CHECK_UN_MATCH);
        }

    }

    // 스프링 시큐리티로 로그인시 사용자 조회를 할수있게 해주는 메소드
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User result = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID '" + userId + "' not found"));
        UserDetailsDto user = new UserDetailsDto(result);
        return user;
    }



}
