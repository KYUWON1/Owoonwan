package com.example.owoonwan.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(RedissonAutoConfiguration config) throws IOException {
        return config.redisson();
    }
}
