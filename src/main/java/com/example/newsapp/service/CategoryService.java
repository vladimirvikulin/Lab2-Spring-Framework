package com.example.newsapp.service;

import com.example.newsapp.model.Category;
import com.example.newsapp.model.News;
import com.example.newsapp.repository.CategoryDAO;
import com.example.newsapp.repository.NewsDAO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryDAO categoryDAO;
    private final NewsDAO newsDAO;

    public CategoryService(CategoryDAO categoryDAO, NewsDAO newsDAO) {
        this.categoryDAO = categoryDAO;
        this.newsDAO = newsDAO;
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryDAO.findById(id);
    }

    public List<Category> getCategoryByKeyword(String keyword) {
        return categoryDAO.findByName(keyword);
    }

    public Long createCategory(String name) {
        Category category = new Category();
        category.setName(name);

        return categoryDAO.save(category);
    }

    @Transactional
    public Long[] createCategories(Object[] names) {
        Long[] ids = new Long[names.length];
        for (int i = 0; i < names.length; i++) {
            ids[i] = createCategory((String) names[i]);
        }
        return ids;
    }

    public boolean updateCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);

        return categoryDAO.update(category);
    }

    @Transactional
    public boolean deleteCategory(Category category) {
        List<News> news = newsDAO.findByCategory(category);
        return newsDAO.deleteAll(news) && categoryDAO.delete(category);
    }
}