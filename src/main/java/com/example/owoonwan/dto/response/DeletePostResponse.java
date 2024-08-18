package com.example.owoonwan.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletePostResponse {
    private Long postId;
    private String userId;
    private Date deletedAt;
}
