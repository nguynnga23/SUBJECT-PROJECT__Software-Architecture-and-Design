package com.example.bookservice.controller;

import com.example.bookservice.dto.CategoryDTO;
import com.example.bookservice.entity.Category;
import com.example.bookservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/book-service/categories")
public class CategoryController {

    @Autowired CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO category) {

        Category savedCategory = new Category();
        savedCategory.setName(category.name());

        return ResponseEntity.ok(categoryService.addCategory(savedCategory));
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<?> updateCategory(@PathVariable UUID category_id, @RequestBody CategoryDTO category) {
        Category savedCategory = categoryService.getCategoryById(category_id).get();
        savedCategory.setName(category.name());
        return ResponseEntity.ok(categoryService.updateCategory(savedCategory));
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID category_id) {
        boolean isDeleted = categoryService.deleteCategory(category_id);

        if(isDeleted){
            return ResponseEntity.ok(Map.of("message", "Category deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Category not found"));
        }
    }
}
