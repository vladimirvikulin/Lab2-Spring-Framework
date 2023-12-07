package com.example.newsapp.controller;

import com.example.newsapp.controller.request.CategoryRequest;
import com.example.newsapp.model.Category;
import com.example.newsapp.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    @Operation(
            summary = "Get all categories",
            description = "Get all categories or error if categories were not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categories not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping("/category")
    @Operation(
            summary = "Create a category",
            description = "Create a category and get it or get an error if input is invalid"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
    })
    public ResponseEntity<?> createCategory (@Valid @RequestBody CategoryRequest categoryRequest) {
        String name = categoryRequest.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(name));
    }

    @DeleteMapping("/category/{id}")
    @Operation(
            summary = "Delete a category",
            description = "Delete a category or get an error if category was not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }

        categoryService.deleteCategory(category.get());
        return ResponseEntity.status(HttpStatus.OK).body("Deleted successfully");
    }
}
