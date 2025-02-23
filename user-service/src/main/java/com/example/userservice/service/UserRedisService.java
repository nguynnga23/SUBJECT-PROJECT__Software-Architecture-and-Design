package com.example.userservice.service;

public interface UserRedisService {
    void save(String key, Object value, long timeout);
    Object get(String key);
    void delete(String key);

}
