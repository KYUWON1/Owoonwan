package com.example.owoonwan.dto.response;

import com.example.owoonwan.dto.dto.DeletePostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "게시글 삭제 응답 DTO")
public class DeletePostResponse {

    @Schema(description = "삭제된 게시글 ID", example = "12345")
    private Long postId;

    @Schema(description = "삭제 요청을 보낸 사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "게시글이 삭제된 시각", example = "2023-08-25T15:30:00Z")
    private Date deletedAt;

    public static DeletePostResponse from(DeletePostDto data, String userId) {
        return DeletePostResponse.builder()
                .deletedAt(data.getDeletedAt())
                .postId(data.getPostId())
                .userId(userId)
                .build();
    }
}
