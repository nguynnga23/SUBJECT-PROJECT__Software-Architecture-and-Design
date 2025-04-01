package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.config.AppConfig;
import com.example.inventoryservice.dto.BookCopyRequest;
import com.example.inventoryservice.dto.BookDTO;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.repositories.InventoryRepository;
import com.example.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;;

    @Override
    public BookDTO getBookById(UUID bookId) {
        String url = "http://localhost:8080/api/book/" + bookId;
        ResponseEntity<BookDTO> response = restTemplate.getForEntity(url, BookDTO.class);
        return response.getBody();
    }

    @Override
    public Inventory addBookToInventory(BookCopyRequest bookCopyRequest) throws Exception {
       return null;
    }

    @Override
    public Inventory updateBookQuantity(UUID bookId, String action) {
        Inventory inventory = inventoryRepository.findById(bookId).orElse(null);
        switch (action){
            case "borrow":
                inventory.setAvailable(inventory.getAvailable() - 1);
                inventory.setBorrowed(inventory.getBorrowed() + 1);
                break;
            case "return":
                inventory.setAvailable(inventory.getAvailable() + 1);
                inventory.setBorrowed(inventory.getBorrowed() - 1);
                break;
            case "lost":
                inventory.setLost(inventory.getLost() + 1);
                inventory.setBorrowed(inventory.getBorrowed() - 1);
                break;
            case "damaged":
                inventory.setDamaged(inventory.getDamaged() + 1);
                inventory.setBorrowed(inventory.getBorrowed() - 1);
                break;
        }
        inventoryRepository.save(inventory);
        return null;
    }

    @Override
    public Inventory getInventory(UUID bookId) {
        return inventoryRepository.findById(bookId).orElse(null);
    }

    @Override
    public boolean removeBookFromInventory(UUID bookId) {
        return false;
    }
}
