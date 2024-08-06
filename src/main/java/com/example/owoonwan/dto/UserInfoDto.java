package com.example.owoonwan.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {
    private String userId;
    private String nickname;
    private String phoneNumber;
    private Date createdAt;
}
