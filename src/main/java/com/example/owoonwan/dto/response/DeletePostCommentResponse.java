package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.DeletePostCommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeletePostCommentResponse {
    private Long commentId;

    public static DeletePostCommentResponse from(DeletePostCommentDto dto){
        return new DeletePostCommentResponse(dto.getCommentId());
    }
}
