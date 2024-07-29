package com.example.owoonwan.exception;

import lombok.*;
import com.example.owoonwan.type.ErrorCode;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserException extends RuntimeException{
    private ErrorCode errorCode;
    private String description;

    public UserException(ErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }
}
