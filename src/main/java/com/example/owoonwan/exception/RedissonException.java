package com.example.owoonwan.exception;

import com.example.owoonwan.type.ErrorCode;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedissonException extends RuntimeException{
    private ErrorCode errorCode;
    private String description;

    public RedissonException(ErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }
}
