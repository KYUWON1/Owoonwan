package com.example.owoonwan.dto.response;

import com.example.owoonwan.type.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "에러 응답 DTO")
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "INVALID_INPUT")
    private ErrorCode errorCode;

    @Schema(description = "에러 메시지", example = "입력 값이 유효하지 않습니다.")
    private String errorMessage;
}
