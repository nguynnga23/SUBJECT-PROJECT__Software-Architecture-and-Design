package com.example.inventoryservice.repositories;

import com.example.inventoryservice.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {
    List<BookCopy> findByBookId(UUID bookId);
}
