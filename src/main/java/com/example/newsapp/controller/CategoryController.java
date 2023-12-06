package com.example.newsapp.controller;

import com.example.newsapp.controller.request.CategoryRequest;
import com.example.newsapp.model.Category;
import com.example.newsapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categories not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping("/category")
    public ResponseEntity<?> createCategory (@RequestBody CategoryRequest categoryRequest) {
        String name = categoryRequest.getName();
        if (name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expected not null name");
        }
        if (name.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expected not blank name");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(name));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }

        categoryService.deleteCategory(category.get());
        if (!categoryService.exists(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Deleted successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }
}
