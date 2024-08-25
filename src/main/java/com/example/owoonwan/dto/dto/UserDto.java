package com.example.owoonwan.dto.dto;

import com.example.owoonwan.type.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 정보 DTO")
public class UserDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "사용자 비밀번호", example = "encrypted_password")
    private String password;

    @Schema(description = "사용자 역할", example = "ROLE_USER")
    private UserRole role;
}
