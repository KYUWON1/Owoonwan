package com.example.owoonwan.jwt;

import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.UserDetailsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Long expiredMs = 10 * 60 * 60 * 1000L;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // JSON 데이터를 파싱하여 userId와 password를 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestMap;

        try {
            requestMap = objectMapper.readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            throw new AuthenticationException("Invalid request payload") {};
        }

        String userId = requestMap.get("userId");
        String password = requestMap.get("password");

        System.out.println(userId + ":" + password);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain
            , Authentication authentication
    ) throws IOException {
        UserDetailsDto user =
                (UserDetailsDto) authentication.getPrincipal();
        // userId를 반환
        String userId = user.getUsername();

        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter =
                authorities.iterator();
        GrantedAuthority auth = iter.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwtToken(userId,role,expiredMs);

        response.addHeader("Authorization","Bearer "+token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"success\": true, \"message\": \"Authentication successful\"}");
    }
}
