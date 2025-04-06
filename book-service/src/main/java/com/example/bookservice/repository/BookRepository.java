package com.example.bookservice.repository;

import com.example.bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    boolean existsByBookCode(String bookCode);
    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN b.category c " +
            "LEFT JOIN b.authors a " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchBooks(@Param("query") String keyword);

    Optional<Book> findByBookCode(String bookCode);
}
