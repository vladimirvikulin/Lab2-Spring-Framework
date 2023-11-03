package com.example.newsapp.controller;

import com.example.newsapp.model.News;
import com.example.newsapp.model.Category;
import com.example.newsapp.service.NewsService;
import com.example.newsapp.service.CategoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {
    @Autowired //DI field
    private NewsService newsService;

    private CategoryService categoryService;

    @Autowired //DI setter
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<News> allNews = newsService.getAllNews();
        model.addAttribute("news", allNews);
        List<Category> allCategories = categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "admin";
    }

    @GetMapping("/admin/createNews")
    public String createNewsForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("news", new News());
        return "createNewsForm";
    }

    @PostMapping("/admin/createNews")
    public String createNews(@ModelAttribute News news) {
        newsService.createNews(news);
        return "redirect:/admin";
    }

    @GetMapping("/admin/createCategory")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "createCategoryForm";
    }

    @PostMapping("/admin/createCategory")
    public String createCategory(@ModelAttribute Category category) {
        categoryService.createCategory(category);
        return "redirect:/admin";
    }
    @PostMapping("/admin/deleteNews/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsService.deleteNewsById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin";
    }
}