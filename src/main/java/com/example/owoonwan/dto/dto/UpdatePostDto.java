package com.example.owoonwan.dto.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePostDto {
    private String userId;
    private Long postId;
}
