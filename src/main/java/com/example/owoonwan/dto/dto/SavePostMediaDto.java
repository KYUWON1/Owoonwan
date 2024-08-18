package com.example.owoonwan.dto.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavePostMediaDto {
    private Long mediaId;
    private String url;
}