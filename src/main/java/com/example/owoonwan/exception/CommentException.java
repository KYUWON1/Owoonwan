package com.example.owoonwan.exception;

import com.example.owoonwan.type.ErrorCode;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentException extends RuntimeException{
    private ErrorCode errorCode;
    private String description;

    public CommentException(ErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }
}
