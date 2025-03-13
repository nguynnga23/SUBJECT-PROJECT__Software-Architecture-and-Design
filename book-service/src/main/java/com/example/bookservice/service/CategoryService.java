package com.example.bookservice.service;

import com.example.bookservice.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category addCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryById(UUID id);
    Category updateCategory(Category category);
    boolean deleteCategory(UUID id);
}
