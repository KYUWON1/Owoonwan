package com.example.owoonwan.dto;

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
