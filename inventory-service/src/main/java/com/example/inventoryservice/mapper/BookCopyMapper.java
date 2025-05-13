package com.example.inventoryservice.mapper;

import com.example.inventoryservice.client.BookServiceClient;
import com.example.inventoryservice.dto.BookCopyDTO;
import com.example.inventoryservice.dto.BookDTO;
import com.example.inventoryservice.entity.BookCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class BookCopyMapper {
    private final BookServiceClient bookServiceClient;

    @Autowired
    public BookCopyMapper(BookServiceClient bookServiceClient) {
        this.bookServiceClient = bookServiceClient;
    }

    public BookCopyDTO mapToDto(BookCopy bookCopy) {
        BookCopyDTO bookCopyDTO = new BookCopyDTO();
        bookCopyDTO.setId(bookCopy.getId());
        bookCopyDTO.setBook(bookServiceClient.getBookById(bookCopy.getBookId()));
        bookCopyDTO.setCopyCode(bookCopy.getCopyCode());
        bookCopyDTO.setLocation(bookCopy.getLocation());
        bookCopyDTO.setStatus(String.valueOf(bookCopy.getStatus()));
        return bookCopyDTO;
    }
}
