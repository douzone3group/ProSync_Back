package com.douzone.prosync.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;


    public String getRefreshToken(String key) {
        //opsForValue : Strings를 쉽게 Serialize / Deserialize 해주는 Interface
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }



    public void setRefreshToken(String key){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key,"0", Duration.ofSeconds(refreshTokenValidityInSeconds));
    }

    public void removeRefreshToken(String key) {
        redisTemplate.delete(key);
    }


}
