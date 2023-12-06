package com.example.newsapp.service;

import com.example.newsapp.model.Category;
import com.example.newsapp.model.News;
import com.example.newsapp.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Page<News> getNewsByCategoryId(Long categoryId, int page, int size) {
        Optional<Category> category = categoryService.getCategoryById(categoryId);
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByCategory(category.orElseThrow(), pageable);
    }
    
    public Page<News> searchNews(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByTitleContaining(keyword, pageable);
    }

    /* public void createNews(News news) {
        Category category = news.getCategory();
        if (category.getId() == null) {
            category = categoryService.createCategory(category);
        }
        news.setCategory(category);

        newsRepository.save(news);
    } */
    
    public void deleteNewsById(Long id) {
        newsRepository.deleteById(id);
    }

    /* public void editNews(News updatedNews) {
        Category category = updatedNews.getCategory();
        if (category.getId() == null) {
            category = categoryService.createCategory(category);
        }
        updatedNews.setCategory(category);

        newsRepository.save(updatedNews);
    } */

}
