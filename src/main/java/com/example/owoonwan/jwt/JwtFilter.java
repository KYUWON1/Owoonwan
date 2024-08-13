package com.example.owoonwan.jwt;

import com.example.owoonwan.domain.User;
import com.example.owoonwan.dto.dto.UserDetailsDto;
import com.example.owoonwan.exception.VerifyException;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.type.UserRole;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 Authorization 정보를 받아서 저장
        String authorization = request.getHeader("Authorization");

        try{
            // 토큰 형식 검증 없거나, 잘못된 형식이면 다음 Filter
            if(authorization == null || !authorization.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            // 인증 부분만 파싱해서 가져옴
            String token = authorization.split(" ")[1];
            // 유효기간 확인
            if(jwtUtil.isExpiredToken(token)){
                throw new ExpiredJwtException(null,null,"Token is Expired");
            }

            String userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);

            User user = User.builder()
                    .userId(userId)
                    .password("tempPassword")
                    .role(UserRole.valueOf(role))
                    .build();

            UserDetailsDto userDetail = new UserDetailsDto(user);

            Authentication authToken =
                    new UsernamePasswordAuthenticationToken(userDetail
                            ,null,userDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request,response);
        }catch(ExpiredJwtException e){
            throw new VerifyException(ErrorCode.JWT_TOKEN_EXPIRED);
        }catch(Exception e){
            throw new VerifyException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
