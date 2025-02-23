package com.example.userservice.service.impl;

import com.example.userservice.service.UserRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserRedisServiceImpl implements UserRedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
