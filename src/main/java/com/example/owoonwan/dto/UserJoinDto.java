package com.example.owoonwan.dto;

import com.example.owoonwan.domain.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJoinDto {
    private String userId;
    private String nickName;

    @Builder
    public static UserJoinDto fromEntity(User user){
        return UserJoinDto.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .build();
    }
}
