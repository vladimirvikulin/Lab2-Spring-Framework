package com.example.newsapp.controller;

import com.example.newsapp.controller.request.*;
import com.example.newsapp.model.News;
import com.example.newsapp.model.Category;
import com.example.newsapp.service.NewsService;
import com.example.newsapp.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Controller
public class NewsController {
    private final NewsService newsService;
    private final CategoryService categoryService;

    @Autowired
    public NewsController(NewsService newsService, CategoryService categoryService) {
        this.newsService = newsService;
        this.categoryService = categoryService;
    }

    @GetMapping("/news")
    public ResponseEntity<?> getAllNews(
            @RequestParam(value = "categoryId", defaultValue = "0", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size
    ) {
        if (categoryId != 0 && categoryService.getCategoryById(categoryId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
        try {
            Page<News> news = categoryId == 0 ? newsService.getAllNews(page, size) :
                    newsService.getNewsByCategoryId(categoryId, page, size);
            if (news.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(news.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }

    @GetMapping("/news/search")
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
    public ResponseEntity<?> getNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.getNewsById(id);
        if (news.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found");
        return ResponseEntity.status(HttpStatus.OK).body(news.get());
    }

    private ResponseEntity<?> saveNews(@RequestBody NewsRequest newsRequest, Long id, HttpStatus status) {
        try {
            String title = newsRequest.getTitle();
            String content = newsRequest.getContent();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(newsRequest.getDate(), formatter);
            Optional<Category> category = categoryService.getCategoryById(Long.parseLong(newsRequest.getCategoryId()));

            return ResponseEntity.status(status).body(newsService.saveNews(id, title, content, date, category));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expected not null fields");
        } catch (DateTimeParseException | NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot parse given input");
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }

    @PostMapping("/news")
    public ResponseEntity<?> createNews(@RequestBody NewsRequest newsRequest) {
        return saveNews(newsRequest, 0L, HttpStatus.CREATED);
    }

    @PutMapping("/news/{id}")
    public ResponseEntity<?> updateNews(
            @PathVariable Long id,
            @RequestBody NewsRequest newsRequest) {
        Optional<News> news = newsService.getNewsById(id);
        if (news.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found");
        }

        return saveNews(newsRequest, id, HttpStatus.OK);
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<?> deleteNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.getNewsById(id);
        if (news.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found");
        }

        newsService.deleteNews(news.get());
        if (!newsService.exists(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Deleted successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }
}
