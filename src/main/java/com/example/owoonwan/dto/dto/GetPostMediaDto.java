package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostMedia;
import com.example.owoonwan.type.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "게시글의 미디어 정보를 담고 있는 DTO")
public class GetPostMediaDto {

    @Schema(description = "게시글 ID", example = "123")
    private Long postId;

    @Schema(description = "미디어 정보 리스트")
    private List<MediaInfo> mediaInfos;

    public static GetPostMediaDto fromDomain(Long postId, List<PostMedia> postMediaList) {
        List<MediaInfo> mediaInfos = postMediaList.stream()
                .map(MediaInfo::fromDomain)
                .collect(Collectors.toList());

        return GetPostMediaDto.builder()
                .postId(postId)
                .mediaInfos(mediaInfos)
                .build();
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "미디어 정보")
    public static class MediaInfo {

        @Schema(description = "미디어 파일의 URL", example = "https://example.com/media/12345")
        private String url;

        @Schema(description = "미디어의 타입 (이미지, 비디오 등)", example = "IMAGE")
        private MediaType type;

        @Schema(description = "미디어의 순서", example = "1")
        private Integer sequence;

        @Schema(description = "미디어의 마지막 수정일", example = "2024-08-25T10:15:30")
        private Date updatedAt;

        public static MediaInfo fromDomain(PostMedia postMedia) {
            return MediaInfo.builder()
                    .url(postMedia.getUrl())
                    .type(postMedia.getType())
                    .sequence(postMedia.getSequence())
                    .updatedAt(postMedia.getUpdatedAt())
                    .build();
        }
    }
}
