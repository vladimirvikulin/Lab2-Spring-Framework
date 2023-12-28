package com.example.newsapp.controller;

import com.example.newsapp.controller.request.CategoryRequest;
import com.example.newsapp.controller.request.CategoryRequestList;
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

import java.util.Arrays;
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

    @GetMapping("/categories/search")
    @Operation(
            summary = "Get categories by keyword",
            description = "Get categories which satisfy a keyword or error if categories were not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> getCategoriesByKeyword(
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword
    ) {
        if (keyword.isBlank()) {
            return getAllCategories();
        }
        List<Category> categories = categoryService.getCategoryByKeyword(keyword);
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categories not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping("/category")
    @Operation(
            summary = "Create a category",
            description = "Create a category and get its id or get an error if input is invalid"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> createCategory (@Valid @RequestBody CategoryRequest categoryRequest) {
        String name = categoryRequest.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(name));
    }

    @PostMapping("/categories")
    @Operation(
            summary = "Create categories",
            description = "Create categories and get its ids or get an error if input is invalid"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> createCategories (@Valid @RequestBody CategoryRequestList categoryRequest) {
        Object[] names = Arrays.stream(categoryRequest.getCategories()).map(CategoryRequest::getName).toArray();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategories(names));
    }

    @PutMapping("/category/{id}")
    @Operation(
            summary = "Update a category",
            description = "Update a category or get an error if category was not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> updateCategory (
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
        String name = categoryRequest.getName();
        if (!categoryService.updateCategory(id, name)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Updated successfully");
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
        if (categoryService.getCategoryById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Deleted successfully");
    }
}
