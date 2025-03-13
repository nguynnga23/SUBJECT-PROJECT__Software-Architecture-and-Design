package com.example.bookservice.controller;

import com.example.bookservice.dto.CategoryDTO;
import com.example.bookservice.entity.Category;
import com.example.bookservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO category) {

        Category savedCategory = new Category();
        savedCategory.setName(category.name());

        return ResponseEntity.ok(categoryService.addCategory(savedCategory));
    }
}
