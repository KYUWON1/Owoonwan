package com.example.owoonwan.dto.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class DeletePostDto {
    private Long postId;
    private Date deletedAt;
}
