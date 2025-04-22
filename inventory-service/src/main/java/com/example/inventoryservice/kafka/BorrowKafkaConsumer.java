package com.example.inventoryservice.kafka;

import com.example.inventoryservice.dto.ReaderRequestDTO;
import com.example.inventoryservice.dto.ReaderRequestDetailDTO;
import com.example.inventoryservice.enums.Status;
import com.example.inventoryservice.service.BookCopyService;
import com.example.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BorrowKafkaConsumer {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BookCopyService bookCopyService;
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic.borrowing-inventory}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeBorrowCreatedEvent(String message) {
        System.out.println("Received BorrowCreated event: " + message);
        try {
            System.out.println("ReaderRequestDTO: ");
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
            ReaderRequestDTO readerRequestDTO = objectMapper.readValue(message, ReaderRequestDTO.class);
            System.out.println("ReaderRequestDTO: " + readerRequestDTO);
            for (ReaderRequestDetailDTO detail : readerRequestDTO.getReaderRequestDetails()) {
                inventoryService.updateBookAvailability(detail.getBookCopyId()); // Decrease available count
                bookCopyService.updateBookCopyStatus(detail.getBookCopyId(), Status.BORROWED); // Update status to Borrowed
            }
        } catch (Exception e) {
            System.err.println("Error processing BorrowCreated event: " + e.getMessage());
        }
    }
}
