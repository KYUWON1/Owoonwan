package com.example.owoonwan.dto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "사용자 정보 상세 DTO")
public class UserInfoDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "닉네임", example = "JohnDoe")
    private String nickName;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "계정 생성일자", example = "2024-08-25T08:42:10.446Z")
    private Date createdAt;
}
