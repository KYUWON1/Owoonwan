package com.example.owoonwan.dto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostDto {
    private String userId;
    private Long postId;
}
