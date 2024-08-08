package com.example.owoonwan.service;

import com.example.owoonwan.domain.PostMedia;
import com.example.owoonwan.dto.SavePostMediaDto;
import com.example.owoonwan.exception.MediaException;
import com.example.owoonwan.repository.PostMediaRepository;
import com.example.owoonwan.repository.PostRepository;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.type.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostMediaService {
    private final PostMediaRepository postMediaRepository;
    private final PostRepository postRepository;
    private final S3Service s3Service;

    public SavePostMediaDto savePostMedia(
            Long postId,
            List<MultipartFile> files
    ) throws IOException {
        SavePostMediaDto result = new SavePostMediaDto();

        fileListIsValid(files,postId);
        Integer order = 1;

        for(MultipartFile file : files){
            String fileName = file.getOriginalFilename();
            MediaType type = checkFileExtensionAndSort(fileName,postId);
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

     private MediaType checkFileExtensionAndSort(String fileName,Long postId){
        int lastDotIndex = fileName.lastIndexOf(".");
        if(lastDotIndex == -1){
            postRepository.deleteById(postId);
            throw new MediaException(ErrorCode.FILE_EXTENSION_NOT_EXIST);
        }
        String extension = fileName.substring(lastDotIndex+1).toLowerCase();

        if(IMAGE_EXTENSIONS.contains(extension)){
            return MediaType.IMAGE;
        }else if(VIDEO_EXTENSIONS.contains(extension)){
            return MediaType.VIDEO;
        }else{
            postRepository.deleteById(postId);
            throw new MediaException(ErrorCode.FILE_EXTENSION_UNKNOWN);
        }
     }

     private void fileListIsValid(List<MultipartFile> files,Long postId){
         // files 이 빈상태로 날라오면 size는 0이 아님. 별도의 예외처리
         if (files == null || files.isEmpty() || files.stream().allMatch(file -> file.isEmpty())) {
             postRepository.deleteById(postId);
             throw new MediaException(ErrorCode.FILE_IS_EMPTY);
         }
     }

    private static final List<String> IMAGE_EXTENSIONS = List.of(
            "jpg", "jpeg", "png", "gif", "bmp", "tif", "tiff", "webp", "svg", "heic"
    );

    private static final List<String> VIDEO_EXTENSIONS = List.of(
            "mp4", "avi", "mkv", "mov", "wmv", "flv", "webm", "mpeg", "mpg", "3gp",
            "ogg", "m4v", "asf", "vob", "rmvb"
    );
}
