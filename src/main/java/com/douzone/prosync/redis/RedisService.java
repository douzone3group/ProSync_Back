package com.douzone.prosync.redis;

import com.douzone.prosync.constant.ConstantPool;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.douzone.prosync.constant.ConstantPool.*;

@Service
@RequiredArgsConstructor
public class RedisService implements TokenStorageService {

    private final RedisTemplate redisTemplate;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;


    public String getRefreshToken(String key) {
        //opsForValue : Strings를 쉽게 Serialize / Deserialize 해주는 Interface
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }


 
    public void setRefreshToken(String key,String value){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key,value, Duration.ofSeconds(refreshTokenValidityInSeconds));
    }

    public void removeRefreshToken(String key) {
        redisTemplate.delete(key);
    }

    public void setEmailCertificationNumber(String key, String certificationNumber) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, certificationNumber, Duration.ofSeconds(EMAIL_CERTIFICATIONNUMBER_DURATION));
    }

    public String getEmailCertificationNumber(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void removeEmailCertificationNumber(String key) {
        redisTemplate.delete(key);
    }




}