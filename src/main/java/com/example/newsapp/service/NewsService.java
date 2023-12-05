package com.example.newsapp.service;

import com.example.newsapp.model.Category;
import com.example.newsapp.model.News;
import com.example.newsapp.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final CategoryService categoryService;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public NewsService(NewsRepository newsRepository, CategoryService categoryService) {
        this.newsRepository = newsRepository;
        this.categoryService = categoryService;
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public News getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public List<News> getNewsByCategoryId(Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return newsRepository.findByCategory(category);
    }
    
    public List<News> searchNews(String keyword) {
        return newsRepository.findByTitleContaining(keyword);
    }

    public News createNews(News news) {
        Category category = news.getCategory();
        if (category.getId() == null) {
            category = categoryService.createCategory(category);
        }
        news.setCategory(category);
    
        news.setId(idGenerator.getAndIncrement());
        return newsRepository.save(news);
    }
    
    public void deleteNewsById(Long id) {
        newsRepository.deleteById(id);
    }

    public void editNews(News updatedNews) {
        Category category = updatedNews.getCategory();
        if (category.getId() == null) {
            category = categoryService.createCategory(category);
        }
        updatedNews.setCategory(category);

        newsRepository.save(updatedNews);
    }

}