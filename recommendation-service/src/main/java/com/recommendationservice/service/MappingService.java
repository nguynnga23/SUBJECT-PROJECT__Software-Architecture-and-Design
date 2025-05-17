package com.recommendationservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class MappingService {
    private Map<String, Integer> user2idx;
    private Map<String, Integer> book2idx;
    private Map<Integer, String> idx2book;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Load user2idx
        try (InputStream is = getClass().getResourceAsStream("/mappings/user2idx.json")) {
            user2idx = mapper.readValue(is, new TypeReference<Map<String, Integer>>() {});
        }
        // Load book2idx
        try (InputStream is = getClass().getResourceAsStream("/mappings/book2idx.json")) {
            book2idx = mapper.readValue(is, new TypeReference<Map<String, Integer>>() {});
        }
        // Create reverse mapping for books
        idx2book = new HashMap<>();
        book2idx.forEach((key, value) -> idx2book.put(value, key));
    }

    public Integer getUserIndex(String userId) {
        return user2idx.get(userId);
    }

    public Map<String, Integer> getBook2idx() {
        return book2idx;
    }

    public String getBookId(int index) {
        return idx2book.get(index);
    }
}