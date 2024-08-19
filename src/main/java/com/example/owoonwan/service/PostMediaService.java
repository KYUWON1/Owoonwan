package com.example.owoonwan.service;

import com.example.owoonwan.domain.PostMedia;
import com.example.owoonwan.dto.dto.GetPostMediaDto;
import com.example.owoonwan.dto.dto.SavePostMediaDto;
import com.example.owoonwan.dto.dto.deletePostMediaDto;
import com.example.owoonwan.exception.MediaException;
import com.example.owoonwan.repository.jpa.PostMediaRepository;
import com.example.owoonwan.repository.jpa.PostRepository;
import com.example.owoonwan.type.ErrorCode;
import com.example.owoonwan.type.MediaType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostMediaService {
    private final PostMediaRepository postMediaRepository;
    private final S3Service s3Service;
    private final RedisTemplate<String,GetPostMediaDto> redisTemplate;

    @Transactional
    public SavePostMediaDto savePostMedia(
            Long postId,
            List<MultipartFile> files
    ) throws IOException {
        SavePostMediaDto result = saveFileListToDbAndS3(postId, files);
        return result;
    }

    @Transactional
    public GetPostMediaDto getPostMedium(Long postId) {
        String cacheKey = "postMediaCache:" + postId;
        ValueOperations<String,GetPostMediaDto> valueOps = redisTemplate.opsForValue();
        GetPostMediaDto cachedMedia = valueOps.get(cacheKey);

        if(cachedMedia != null){
            redisTemplate.expire(cacheKey, Duration.ofMinutes(10));
            log.info("Cache hit for media ID {}. TTL reset to 10 minutes.",
                    postId);
            return cachedMedia;
        }

        log.info("Cache miss for media ID {}, querying database...", postId);
        List<PostMedia> allMediaByPostId =
                postMediaRepository.findAllByPostId(postId);

        if(allMediaByPostId.isEmpty()){
            log.info("No media found for post ID {}. Skipping cache.", postId);
            return GetPostMediaDto.builder().postId(postId).build();
        }

        GetPostMediaDto getPostMediaDto = GetPostMediaDto.fromDomain(postId, allMediaByPostId);
        valueOps.set(cacheKey,getPostMediaDto,Duration.ofMinutes(10));
        return getPostMediaDto;
    }

    @Transactional
    public deletePostMediaDto deletePostMedia(Long postId) {
        deleteRedisCache(postId);
        List<PostMedia> allByPostId =
                postMediaRepository.findAllByPostId(postId);

        // 있다면 s3에서 삭제
        if(!allByPostId.isEmpty()){
            for(PostMedia post : allByPostId){
                s3Service.deleteFile(post.getUrl());
            }
        }
        //postMediaRepository.deleteAllInBatch(allByPostId);
        postMediaRepository.deleteByPostId(postId);

        return deletePostMediaDto.builder()
                .postId(postId)
                .deletedAt(new Date())
                .build();
    }

    @Transactional
    public SavePostMediaDto updatePostMedia(Long postId, List<MultipartFile> files) throws IOException {
        List<PostMedia> existingMedia =
                postMediaRepository.findAllByPostId(postId);

        // 있다면 s3에서 삭제
        if(!existingMedia.isEmpty()){
            for(PostMedia post : existingMedia){
                s3Service.deleteFile(post.getUrl());
            }
            postMediaRepository.deleteAll(existingMedia);
        }

        SavePostMediaDto result = saveFileListToDbAndS3(postId, files);

        // 캐시 갱신
        deleteRedisCache(postId);

        List<PostMedia> updatedMediaList =
                postMediaRepository.findAllByPostId(postId);
        putRedisCache(postId,updatedMediaList);

        return result;
    }

    private void deleteRedisCache(Long postId){
        String cacheKey = "postMediaCache:" + postId;
        redisTemplate.delete(cacheKey);
    }

    private void putRedisCache(Long postId,List<PostMedia> updatedMediaList){
        String cacheKey = "postMediaCache:" + postId;
        GetPostMediaDto updatedPostMediaDto = GetPostMediaDto.fromDomain(postId,
                updatedMediaList);
        redisTemplate.opsForValue().set(cacheKey,updatedPostMediaDto,
                Duration.ofMinutes(10));
    }

    private SavePostMediaDto saveFileListToDbAndS3(Long postId,List<MultipartFile> files) throws IOException {
        Integer order = 1;
        SavePostMediaDto result = new SavePostMediaDto();
        for(MultipartFile file : files){
            String filename = file.getOriginalFilename();
            MediaType type = fileExtensionSort(filename);
            String fileUrl = generateFileUrl(filename,type);

            // PostMedia 엔티티 생성 및 저장
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
