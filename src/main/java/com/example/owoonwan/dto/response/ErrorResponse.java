package com.example.owoonwan.dto.response;

import com.example.owoonwan.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse{
    private ErrorCode errorCode;
    private String errorMessage;
}
