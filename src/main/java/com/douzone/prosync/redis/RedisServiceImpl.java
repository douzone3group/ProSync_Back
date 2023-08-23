package com.douzone.prosync.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate redisTemplate;


    public void set(String key, String value, Long seconds) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, Duration.ofSeconds(seconds));
    }

    public String get(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }


}