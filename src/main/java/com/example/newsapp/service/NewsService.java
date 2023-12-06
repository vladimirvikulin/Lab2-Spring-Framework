package com.example.newsapp.service;

import com.example.newsapp.model.Category;
import com.example.newsapp.model.News;
import com.example.newsapp.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final CategoryService categoryService;

    public NewsService(NewsRepository newsRepository, CategoryService categoryService) {
        this.newsRepository = newsRepository;
        this.categoryService = categoryService;
    }

    public Page<News> getAllNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findAll(pageable);
    }

    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public Page<News> getNewsByCategoryId(Long id, int page, int size) {
        Optional<Category> category = categoryService.getCategoryById(id);
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByCategory(category.orElseThrow(), pageable);
    }
    
    public Page<News> searchNews(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByTitleContaining(keyword, pageable);
    }

    public News saveNews(Long id, String title, String content, LocalDate date, Optional<Category> category) {
        if (title.isBlank() || content.isBlank() || date == null) {
            throw new IllegalArgumentException("Expected not blank fields");
        }

        LocalDate currentDate = LocalDate.now();
        if (date.isAfter(currentDate)) {
            throw new IllegalArgumentException("Date should be in the past");
        }

        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }

        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        news.setDate(date);
        news.setCategory(category.get());

        if (id != 0) {
            news.setId(id);
        }

        return newsRepository.save(news);
    }

    public void deleteNews(News news) {
        newsRepository.delete(news);
    }

    public boolean exists(Long id) {
        return newsRepository.existsById(id);
    }
}
