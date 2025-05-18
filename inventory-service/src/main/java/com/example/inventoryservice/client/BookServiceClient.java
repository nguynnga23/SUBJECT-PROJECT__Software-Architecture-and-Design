package com.example.inventoryservice.client;

import com.example.inventoryservice.config.FeignConfig;
import com.example.inventoryservice.dto.BookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "book-service", configuration = FeignConfig.class)
public interface BookServiceClient {
    @GetMapping("/api/v1/book-service/books/{bookId}/exists")
    ResponseEntity<Boolean> checkBookExists(@PathVariable("bookId") String bookId);
    @GetMapping("/api/v1/book-service/books/{bookId}")
    BookDTO getBookById(@PathVariable UUID bookId);
}
