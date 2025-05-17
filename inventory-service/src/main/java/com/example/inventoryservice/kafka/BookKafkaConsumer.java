package com.example.inventoryservice.kafka;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookKafkaConsumer {

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(topics = "${spring.kafka.topic.book}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeBookCreatedEvent(String message) {
        System.out.println("Received BookCreated event: " + message);

        try {
            // Kiểm tra định dạng message
            if (!message.contains(": ")) {
                System.err.println("Invalid message format: " + message);
                return;
            }
            // Tách lấy bookId từ message
            String bookId = message.split(": ")[1];
            UUID parsedBookId = UUID.fromString(bookId);

            // Kiểm tra xem Inventory đã tồn tại chưa
            if (inventoryService.getInventoryByBookId(parsedBookId) != null) {
                System.out.println("Inventory already exists for Book ID: " + parsedBookId);
                return;
            }
            // Tạo Inventory mới nếu chưa tồn tại
            Inventory inventory = new Inventory();
            inventory.setBookId(parsedBookId);
            inventoryService.saveInventory(inventory);
            System.out.println("Inventory created for Book ID: " + parsedBookId);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid UUID format: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to process BookCreated event: " + e.getMessage());
        }
    }

}
