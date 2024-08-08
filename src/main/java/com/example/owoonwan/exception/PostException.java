package com.example.owoonwan.exception;

import com.example.owoonwan.type.ErrorCode;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostException extends RuntimeException{
    private ErrorCode errorCode;
    private String description;

    public PostException(ErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }
}
