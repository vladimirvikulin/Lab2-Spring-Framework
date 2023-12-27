package com.example.newsapp.service;

import com.example.newsapp.model.Category;
import com.example.newsapp.model.News;
import com.example.newsapp.repository.NewsDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private final NewsDAO newsDAO;
    private final CategoryService categoryService;

    public NewsService(NewsDAO newsDAO, CategoryService categoryService) {
        this.newsDAO = newsDAO;
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
        List<News> allNews = newsDAO.findAll();
        return pagedList(allNews, page, size);
    }

    public Optional<News> getNewsById(Long id) {
        return newsDAO.findById(id);
    }

    public List<News> getNewsByCategoryId(Long id, int page, int size) {
        Optional<Category> category = categoryService.getCategoryById(id);
        List<News> allNews = newsDAO.findByCategory(category.orElseThrow());
        return pagedList(allNews, page, size);
    }
    
    public List<News> searchNews(String keyword, int page, int size) {
        List<News> allNews = newsDAO.findByTitleContaining(keyword);
        return pagedList(allNews, page, size);
    }

    private News getNews(Long id, String title, String content, LocalDate date, Category category) {
        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        news.setDate(date);
        news.setCategory(category);
        if (id != 0) {
            news.setId(id);
        }
        return news;
    }

    public Long createNews(String title, String content, LocalDate date, Category category) {
        News news = getNews(0L, title, content, date, category);
        return newsDAO.save(news);
    }

    public boolean updateNews(Long id, String title, String content, LocalDate date, Category category) {
        News news = getNews(id, title, content, date, category);
        return newsDAO.update(news);
    }

    public boolean deleteNews(News news) {
       return newsDAO.delete(news);
    }
}
