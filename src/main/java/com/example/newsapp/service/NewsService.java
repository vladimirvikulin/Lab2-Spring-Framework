package com.example.newsapp.service;

import com.example.newsapp.model.Category;
import com.example.newsapp.model.News;
import com.example.newsapp.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private List<News> pagedList(List<News> list, int page, int size) {
        int start = page * size;
        int end = Math.min(start + size, list.size());
        if (start > end || start < 0) {
            return new ArrayList<>();
        }
        return list.subList(start, end);
    }

    public List<News> getAllNews(int page, int size) {
        List<News> allNews = new ArrayList<>();
        newsRepository.findAll().forEach(allNews::add);
        return pagedList(allNews, page, size);
    }

    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public List<News> getNewsByCategoryId(Long id, int page, int size) {
        Optional<Category> category = categoryService.getCategoryById(id);
        List<News> allNews = newsRepository.findByCategory(category.orElseThrow().getId());
        return pagedList(allNews, page, size);
    }
    
    public List<News> searchNews(String keyword, int page, int size) {
        List<News> allNews = newsRepository.findByTitleContaining(keyword);
        return pagedList(allNews, page, size);
    }

    public Long createNews(String title, String content, LocalDate date, Category category) {
        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        news.setDate(date);
        news.setCategory(category);
        return newsRepository.save(news).getId();
    }

    public boolean updateNews(Long id, String title, String content, LocalDate date, Category category) {
        return newsRepository.update(title, content, date, category.getId(), id) > 0;
    }

    public void deleteNews(News news) {
        newsRepository.delete(news);
    }
}
