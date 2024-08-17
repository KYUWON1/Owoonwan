package com.example.owoonwan.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DeleteUser {
    String userId;
    LocalDate deletedAt;
}
