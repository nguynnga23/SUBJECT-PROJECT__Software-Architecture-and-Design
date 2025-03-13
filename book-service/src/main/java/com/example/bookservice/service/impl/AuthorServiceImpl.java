package com.example.bookservice.service.impl;

import com.example.bookservice.entity.Author;
import com.example.bookservice.repository.AuthorRepository;
import com.example.bookservice.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(UUID id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public Author updateAuthor(Author author) {
        Author newAuthor = authorRepository.findById(author.getId()).get();
        newAuthor.setName(author.getName());
        authorRepository.save(newAuthor);
        return newAuthor;
    }

    @Override
    public boolean deleteAuthor(UUID id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
