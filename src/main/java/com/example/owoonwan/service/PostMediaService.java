package com.example.owoonwan.service;

import com.example.owoonwan.domain.PostMedia;
import com.example.owoonwan.dto.SavePostMediaDto;
import com.example.owoonwan.repository.PostMediaRepository;
import com.example.owoonwan.type.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostMediaService {
    private final PostMediaRepository postMediaRepository;
    private final S3Service s3Service;

    public SavePostMediaDto savePostMedia(
            Long postId,
            List<MultipartFile> files
    ) throws IOException {
        SavePostMediaDto result = new SavePostMediaDto();

        Integer order = 1;

        for(MultipartFile file : files){
            String fileName = file.getOriginalFilename();
            MediaType type = fileExtensionSort(fileName);
            String fileUrl = generateFileUrl(fileName,type);

            PostMedia media = new PostMedia();
            media.setPostId(postId);
            media.setUrl(fileUrl);
            media.setType(type);
            media.setSequence(order);

            String url = s3Service.uploadFile(file, fileUrl);
            PostMedia save = postMediaRepository.save(media);
            if(order == 1){
                result.setMediaId(save.getMediaId());
                result.setUrl(url);
            }
            order++;
        }
        return result;
    }

    private String generateFileUrl(String fileName,MediaType type){
        String mediaType = type.toString();
        String uuid = UUID.randomUUID().toString();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String date = LocalDate.now().toString();
        return date +"/"+mediaType+"/"+uuid+extension;
    }

     private MediaType fileExtensionSort(String fileName){
        int lastDotIndex = fileName.lastIndexOf(".");

        String extension = fileName.substring(lastDotIndex+1).toLowerCase();

        if(IMAGE_EXTENSIONS.contains(extension)){
            return MediaType.IMAGE;
        }else {
            return MediaType.VIDEO;
        }
     }

    private static final List<String> IMAGE_EXTENSIONS = List.of(
            "jpg", "jpeg", "png", "gif", "bmp", "tif", "tiff", "webp", "svg", "heic"
    );

}
