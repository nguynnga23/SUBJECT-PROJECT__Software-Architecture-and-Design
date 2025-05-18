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

import java.util.UUID;

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
            // Định cấu hình ObjectMapper
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);

            // Chuyển đổi JSON thành ReaderRequestDTO
            ReaderRequestDTO readerRequestDTO = objectMapper.readValue(message, ReaderRequestDTO.class);
            System.out.println("ReaderRequestDTO: " + readerRequestDTO);

            // Duyệt qua danh sách request details
            for (ReaderRequestDetailDTO detail : readerRequestDTO.getReaderRequestDetails()) {
                UUID bookCopyId = detail.getBookCopy().getId(); // Lấy ID từ BookCopyDTO
                System.out.println("Processing BookCopy ID: " + bookCopyId);
                // Cập nhật số lượng và trạng thái sách
                String status = readerRequestDTO.getStatus();
                switch (status) {
                    case "PENDING" -> bookCopyService.updateBookCopyStatus(bookCopyId, Status.BORROWED);
                    case "RETURNED", "CANCELED" -> bookCopyService.updateBookCopyStatus(bookCopyId, Status.AVAILABLE);
                    default -> throw new IllegalArgumentException("Invalid status: " + status);
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing BorrowCreated event: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
