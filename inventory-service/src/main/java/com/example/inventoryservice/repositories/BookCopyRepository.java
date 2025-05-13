package com.example.inventoryservice.repositories;

import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {
    List<BookCopy> findByBookId(UUID bookId);
    @Query("SELECT bc.copyCode FROM BookCopy bc WHERE bc.copyCode LIKE 'BC_%' ORDER BY bc.copyCode DESC LIMIT 1")
    String findLatestCopyCode();
    BookCopy findFirstByBookIdAndStatus(UUID bookId, Status status);
    List<BookCopy> findByBookIdAndStatus(UUID bookId, Status status);
    List<BookCopy> findByStatus(Status status);
}
