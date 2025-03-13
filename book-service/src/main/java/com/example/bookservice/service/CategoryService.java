package com.example.bookservice.service;

import com.example.bookservice.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Category addCategory(Category category);
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(UUID id);
    Category getCategoryByName(String categoryName);
    Category updateCategory(Category category);
    boolean deleteCategory(UUID id);
}
