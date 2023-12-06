package com.example.newsapp.service;

import com.example.newsapp.model.Category;
import com.example.newsapp.model.News;
import com.example.newsapp.repository.CategoryRepository;
import com.example.newsapp.repository.NewsRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final NewsRepository newsRepository;

    public CategoryService(CategoryRepository categoryRepository, NewsRepository newsRepository) {
        this.categoryRepository = categoryRepository;
        this.newsRepository = newsRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id) {
        Optional<Category> category = getCategoryById(id);
        if(category.isPresent()) {
            List<News> newsToDelete = newsRepository.findByCategory(category.get());
            for (News news : newsToDelete) {
                newsRepository.deleteById(news.getId());
            }
            categoryRepository.deleteById(id);
        }
    }
}