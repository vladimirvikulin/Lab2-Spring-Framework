package com.example.newsapp.service;

import com.example.newsapp.model.Category;
import com.example.newsapp.model.News;
import com.example.newsapp.repository.CategoryRepository;

import com.example.newsapp.repository.NewsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final NewsRepository newsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CategoryService(NewsRepository newsRepository, CategoryRepository categoryRepository) {
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getCategoryByKeyword(String keyword) {
        TypedQuery<Category> query = entityManager.createNamedQuery("Category.findByName", Category.class);
        query.setParameter("name", keyword);
        return query.getResultList();
    }

    public Long createCategory(String name) {
        Category category = new Category();
        category.setName(name);

        return categoryRepository.save(category).getId();
    }

    @Transactional
    public Long[] createCategories(Object[] names) {
        Long[] ids = new Long[names.length];
        for (int i = 0; i < names.length; i++) {
            ids[i] = createCategory((String) names[i]);
        }
        return ids;
    }

    @Transactional
    public boolean updateCategory(Long id, String name) {
        return categoryRepository.update(name, id) > 0;
    }

    @Transactional
    public void deleteCategory(Category category) {
        List<News> news = newsRepository.findByCategory(category.getId());
        newsRepository.deleteAll(news);
        categoryRepository.delete(category);
    }
}