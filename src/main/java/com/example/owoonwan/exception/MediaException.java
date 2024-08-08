package com.example.owoonwan.exception;

import com.example.owoonwan.type.ErrorCode;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaException extends RuntimeException{
    private ErrorCode errorCode;
    private String description;

    public MediaException(ErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }
}
