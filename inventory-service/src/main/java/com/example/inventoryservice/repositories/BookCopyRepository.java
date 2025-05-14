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

    // Tổng số sách trong kho
    @Query("SELECT COUNT(bc) FROM BookCopy bc")
    Long countTotalBooks();

    // Tổng số sách đang được mượn
    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.status = 'BORROWED'")
    Long countBorrowedBooks();

    // Tổng số sách có sẵn
    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.status = 'AVAILABLE'")
    Long countAvailableBooks();

    // Tổng số sách bị mất/hư hỏng
    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.status IN ('LOST', 'DAMAGED')")
    Long countLostOrDamagedBooks();


}
