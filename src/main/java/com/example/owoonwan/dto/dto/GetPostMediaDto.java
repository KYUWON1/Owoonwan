package com.example.owoonwan.dto.dto;

import com.example.owoonwan.domain.PostMedia;
import com.example.owoonwan.type.MediaType;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPostMediaDto {
    private Long postId;
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
    public static class MediaInfo {
        private String url;
        private MediaType type;
        private Integer sequence;
        private Date updatedAt;

        public static MediaInfo fromDomain(PostMedia postMedia){
            return MediaInfo.builder()
                    .url(postMedia.getUrl())
                    .type(postMedia.getType())
                    .sequence(postMedia.getSequence())
                    .updatedAt(postMedia.getUpdatedAt())
                    .build();
        }
    }
}
