package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Schema(description = "사용자 세부 정보 DTO")
public class UserDetailsDto implements UserDetails {

    @Schema(description = "사용자 정보 DTO")
    private final UserDto user;

    public UserDetailsDto(UserDto user) {
        this.user = user;
    }

    @Override
    @Schema(description = "사용자 권한", example = "ROLE_USER")
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole().toString();
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    @Schema(description = "사용자 비밀번호", example = "encrypted_password")
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @Schema(description = "사용자 ID", example = "user123")
    public String getUsername() {
        return user.getUserId();
    }

    // Other methods (isAccountNonExpired, isAccountNonLocked, etc.) would be implemented here
}
