package com.example.bookservice.controller;

import com.example.bookservice.dto.CategoryDTO;
import com.example.bookservice.entity.Author;
import com.example.bookservice.entity.Category;
import com.example.bookservice.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<?> addAuthor(@RequestBody CategoryDTO category) {

        Author savedAuthor = new Author();
        savedAuthor.setName(category.name());

        return ResponseEntity.ok(authorService.addAuthor(savedAuthor));
    }
}
