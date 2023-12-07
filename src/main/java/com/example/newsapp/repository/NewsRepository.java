package com.example.newsapp.repository;

import com.example.newsapp.model.News;
import com.example.newsapp.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCategory(Category category);
    Page<News> findByCategory(Category category, Pageable pageable);
    Page<News> findByTitleContaining(String keyword, Pageable pageable);
}