package com.example.owoonwan.dto.dto;

import com.example.owoonwan.type.UserRole;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    private String password;
    private UserRole role;
}
