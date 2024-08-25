package com.example.owoonwan.config;

import com.example.owoonwan.jwt.JwtFilter;
import com.example.owoonwan.jwt.JwtUtil;
import com.example.owoonwan.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean // auth Manager Bean으로 등록
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authManager =
                authenticationManager(authenticationConfiguration);
        LoginFilter loginFilter = new LoginFilter(authManager,jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/v1/user/login");

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/user/submit-form",
                                "/api/v1/user/register",
                                "/h2-console/**",
                                "/api/v1/user/login",
                                "/swagger-ui/**",              // Swagger UI 경로 허용
                                "/v3/api-docs/**",            // OpenAPI 문서 경로 허용
                                "/swagger-resources/**",      // Swagger 리소스 경로 허용
                                "/webjars/**"                 // Swagger UI가 사용하는 웹 자르 경로 허용
                        ).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/me",true)
                        .permitAll());

        http
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(loginFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
