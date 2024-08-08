package com.example.owoonwan.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavePostMediaDto {
    Long mediaId;
    String url;
}
