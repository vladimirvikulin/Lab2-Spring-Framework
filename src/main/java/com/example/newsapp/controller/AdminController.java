package com.example.newsapp.controller;

import com.example.newsapp.model.News;
import com.example.newsapp.model.Category;
import com.example.newsapp.service.NewsService;
import com.example.newsapp.service.CategoryService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    private final NewsService newsService;

    private final CategoryService categoryService;

    @Autowired
    public AdminController(NewsService newsService, CategoryService categoryService) {
        this.newsService = newsService;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/createForm")
    public String createForm(@RequestParam String formType, Model model) {
        List<Category> categories = categoryService.getAllCategories();
        switch (formType) {
            case "news":
                model.addAttribute("categories", categories);
                model.addAttribute("news", new News());
                break;
            case "category":
                model.addAttribute("category", new Category());
                break;
            default:
                break;
        }
        model.addAttribute("formType", formType);
        return "createForm";
    }

    @PostMapping("/admin/createNews")
    public String createNews(@ModelAttribute News news) {
        newsService.createNews(news);
        return "redirect:/";
    }

    @PostMapping("/admin/createCategory")
    public String createCategory(@ModelAttribute Category category) {
        categoryService.createCategory(category);
        return "redirect:/";
    }
    @DeleteMapping("/admin/deleteNews/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsService.deleteNewsById(id);
        return "redirect:/";
    }

    @DeleteMapping("/admin/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/";
    }

    @GetMapping("/admin/editNews/{id}")
    public String editNewsForm(@PathVariable Long id, Model model) {
        Optional<News> news = newsService.getNewsById(id);
        news.ifPresent(value -> model.addAttribute("news", value));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "editNews";
    }

    @PutMapping("/admin/editNews/{id}")
    public String editNews(@PathVariable Long id, @ModelAttribute News updatedNews) {
        updatedNews.setId(id);
        newsService.editNews(updatedNews);
        return "redirect:/";
    }
}