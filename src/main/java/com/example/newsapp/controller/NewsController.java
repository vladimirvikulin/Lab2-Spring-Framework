package com.example.newsapp.controller;

import com.example.newsapp.model.News;
import com.example.newsapp.model.Category;
import com.example.newsapp.service.NewsService;
import com.example.newsapp.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class NewsController {
    private final NewsService newsService;
    private final CategoryService categoryService;
    private boolean isAdmin = false;

    @Autowired
    public NewsController(NewsService newsService, CategoryService categoryService) {
        this.newsService = newsService;
        this.categoryService = categoryService;
    }

    private String homePage(Model model, Page<News> news, Long categoryId) {
        int total = news.getTotalPages();
        if (total > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, total)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("news", news);
        List<Category> allCategories = categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("isAdmin", isAdmin);
        return "home";
    }

    @GetMapping("/")
    public String home(
            Model model,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "2", required = false) int size,
            @RequestParam(value = "category", defaultValue = "0", required = false) Long id
    ) {
        Page<News> news = id == 0 ? newsService.getAllNews(page, size) :
                newsService.getNewsByCategoryId(id, page, size);
        return homePage(model, news, id);
    }

    @GetMapping("/news/{id}")
    public String viewNews(@PathVariable Long id, Model model) {
        Optional<News> news = newsService.getNewsById(id);
        news.ifPresent(value -> model.addAttribute("news", value));
        return "news";
    }

    @GetMapping("/search")
    public String searchNews(
            @RequestParam String keyword,
            Model model,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "2", required = false) int size
    ) {
        Page<News> news = newsService.searchNews(keyword, page, size);
        model.addAttribute("news", news.getContent());
        return "home";
    }

    @GetMapping("/changeStatus")
    public String changeUserStatus(Model model) {
        isAdmin = !isAdmin;
        model.addAttribute("isAdmin", isAdmin);
        return "redirect:/";
    }
}
