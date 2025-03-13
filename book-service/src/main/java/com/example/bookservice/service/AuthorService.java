package com.example.bookservice.service;

import com.example.bookservice.entity.Author;

import java.util.List;
import java.util.UUID;

public interface AuthorService {
    Author addAuthor(Author author);
    List<Author> getAllAuthors();
    Author getAuthorById(UUID id);
    Author updateAuthor(Author author);
    boolean deleteAuthor(UUID id);
}
