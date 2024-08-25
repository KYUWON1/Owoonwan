package com.example.owoonwan.config;

import com.example.owoonwan.dto.dto.PostDtoRedisTemplate;
import com.example.owoonwan.dto.dto.GetPostMediaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisCacheConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    // 캐시관련 config bean 생성
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        // 직렬화 지정해주어야함 -> 데이터 오브젝트 같은 값들을 byte로 변환
        RedisCacheConfiguration conf =
                RedisCacheConfiguration.defaultCacheConfig()
                        //.entryTtl(Duration.ofMinutes(10)) // 캐시 만료시간 10분,
                        // @Cacheable 어노테이션에서만 적용됨
                        .disableCachingNullValues() // null 값 저장 방지
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(conf)
                .build();
    }

    @Bean
    public RedisTemplate<String, PostDtoRedisTemplate> postDtoRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, PostDtoRedisTemplate> template = new RedisTemplate<>();
        configureTemplate(template, factory, new Jackson2JsonRedisSerializer<>(PostDtoRedisTemplate.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, GetPostMediaDto> postMediaDtoRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, GetPostMediaDto> template = new RedisTemplate<>();
        configureTemplate(template, factory, new Jackson2JsonRedisSerializer<>(GetPostMediaDto.class));
        return template;
    }


    @Bean
    public CommandLineRunner clearRedisCache(RedisConnectionFactory redisConnectionFactory) {
        // 캐시용임으로 서버 재가동시 데이터 flush
        return args -> {
            redisConnectionFactory.getConnection().flushDb();
            log.info("Redis cache has been cleared on application start for " +
                    "test");
        };
    }

    private void configureTemplate(RedisTemplate template,
                                   RedisConnectionFactory factory,
                                   RedisSerializer serializer) {
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.afterPropertiesSet();
    }
}
