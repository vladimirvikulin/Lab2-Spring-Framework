package com.example.newsapp;

import com.example.newsapp.repository.CategoryRepository;
import com.example.newsapp.repository.NewsRepository;
import com.example.newsapp.service.CategoryService;
import com.example.newsapp.service.NewsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    @Bean
    @Scope("prototype")
    public NewsService newsService(NewsRepository newsRepository, CategoryService categoryService) {
        return new NewsService(newsRepository, categoryService);
    }

    @Bean
    @Scope("prototype")
    public CategoryService categoryService(CategoryRepository categoryRepository, NewsRepository newsRepository) {
        return new CategoryService(categoryRepository, newsRepository);
    }
}
