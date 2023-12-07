package com.example.newsapp.controller;

import com.example.newsapp.controller.request.*;
import com.example.newsapp.model.News;
import com.example.newsapp.model.Category;
import com.example.newsapp.service.NewsService;
import com.example.newsapp.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
public class NewsController {
    private final NewsService newsService;
    private final CategoryService categoryService;

    @Autowired
    public NewsController(NewsService newsService, CategoryService categoryService) {
        this.newsService = newsService;
        this.categoryService = categoryService;
    }

    @GetMapping("/news")
    @Operation(
            summary = "Get all news",
            description = "Get one page of all news or error if news were not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> getAllNews(
            @RequestParam(value = "categoryId", defaultValue = "0", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size
    ) {
        if (categoryId != 0 && categoryService.getCategoryById(categoryId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }

        Page<News> news = categoryId == 0 ? newsService.getAllNews(page, size) :
                    newsService.getNewsByCategoryId(categoryId, page, size);

        if (news.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(news.getContent());
    }

    @GetMapping("/news/search")
    @Operation(
            summary = "Get news by keyword",
            description = "Get one page of news which satisfy a keyword or error if news were not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> getNewsByKeyword(
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size
    ) {
        if (keyword.isBlank()) {
            return getAllNews(0L, page, size);
        }
        Page<News> news = newsService.searchNews(keyword, page, size);
        if (news.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(news.getContent());
    }

    @GetMapping("/news/{id}")
    @Operation(
            summary = "Get a news by ID",
            description = "Get one news by its ID or error if news was not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> getNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.getNewsById(id);
        if (news.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(news.get());
    }

    private ResponseEntity<?> saveNews(NewsRequest newsRequest, Long id, HttpStatus status) {
        String title = newsRequest.getTitle();
        String content = newsRequest.getContent();
        LocalDate date = newsRequest.getDate();
        Long categoryId = newsRequest.getCategoryId();

        Optional<Category> category = categoryService.getCategoryById(categoryId);
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }

        return ResponseEntity.status(status).body(newsService.saveNews(id, title, content, date, category.get()));
    }

    @PostMapping("/news")
    @Operation(
            summary = "Create a news",
            description = "Create a news and get it or get an error if input is invalid or category was not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> createNews(@Valid @RequestBody NewsRequest newsRequest) {
        return saveNews(newsRequest, 0L, HttpStatus.CREATED);
    }

    @PutMapping("/news/{id}")
    @Operation(
            summary = "Update a news",
            description = "Update a news and get it or get an error if input is invalid or news/category was not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> updateNews(
            @PathVariable Long id,
            @Valid @RequestBody NewsRequest newsRequest) {
        Optional<News> news = newsService.getNewsById(id);
        if (news.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found");
        }

        return saveNews(newsRequest, id, HttpStatus.OK);
    }

    @DeleteMapping("/news/{id}")
    @Operation(
            summary = "Delete a news by ID",
            description = "Delete a news by its ID or get an error if news was not found"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> deleteNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.getNewsById(id);
        if (news.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found");
        }

        newsService.deleteNews(news.get());
        if (newsService.exists(id)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Deleted successfully");
    }
}
