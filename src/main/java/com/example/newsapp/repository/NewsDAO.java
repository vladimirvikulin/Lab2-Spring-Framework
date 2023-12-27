package com.example.newsapp.repository;

import com.example.newsapp.model.News;
import com.example.newsapp.model.Category;

import java.util.List;
import java.util.Optional;

public interface NewsDAO {
    List<News> findAll();
    Optional<News> findById(Long id);
    List<News> findByCategory(Category category);
    List<News> findByTitleContaining(String keyword);
    Long save(News news);
    boolean update(News news);
    boolean deleteAll(List<News> news);
    boolean delete(News news);
}