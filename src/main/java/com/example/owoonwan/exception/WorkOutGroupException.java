package com.example.owoonwan.exception;

import com.example.owoonwan.type.ErrorCode;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkOutGroupException extends RuntimeException{
    private ErrorCode errorCode;
    private String description;

    public WorkOutGroupException(ErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }
}
