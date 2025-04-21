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

        String bookId = message.split(": ")[1];
        Inventory inventory = new Inventory();
        inventory.setBookId(UUID.fromString(bookId));
        inventory.setTotalQuantity(0);
        inventory.setAvailable(0);
        inventory.setBorrowed(0);
        inventory.setLost(0);
        inventory.setDamaged(0);
        inventoryService.saveInventory(inventory);
        System.out.println( inventoryService.saveInventory(inventory));
    }
}
