package com.example.owoonwan.utils;

import com.example.owoonwan.dto.UserDetailsDto;
import com.example.owoonwan.exception.VerifyException;
import com.example.owoonwan.type.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {
    public static String getUserIdFromToken(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new VerifyException(ErrorCode.JWT_TOKEN_NOT_CORRECT);
        }

        UserDetailsDto user = (UserDetailsDto) authentication.getPrincipal();
        if(user == null){
            throw new VerifyException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return user.getUsername();
    }
}
