package com.example.bookservice.service;

import com.example.bookservice.entity.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorService {
    Author addAuthor(Author author);
    Optional<Author> getAuthorById(UUID id);
    List<Author> getAllAuthors();
    Author updateAuthor(Author author);
    boolean deleteAuthor(UUID id);
}
