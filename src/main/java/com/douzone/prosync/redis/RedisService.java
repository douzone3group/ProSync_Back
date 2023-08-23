package com.douzone.prosync.redis;

public interface RedisService {

     void set(String key, String value, Long seconds);

     String get(String key);

     void remove(String key);
}