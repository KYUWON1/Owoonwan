package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "사용자 가입 DTO")
public class UserJoinDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "닉네임", example = "JohnDoe")
    private String nickName;

    @Builder
    public static UserJoinDto fromEntity(User user) {
        return UserJoinDto.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .build();
    }
}
