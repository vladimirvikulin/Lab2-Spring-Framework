package com.example.newsapp.repository;

import com.example.newsapp.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDAO {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    List<Category> findByName(String keyword);
    Long save(Category category);
    boolean update(Category category);
    boolean delete(Category category);
}