package com.example.newsapp.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class NewsRequest {
    @NotBlank(message = "Expected any title")
    private String title;
    @NotBlank(message = "Expected any content")
    private String content;
    @NotNull(message = "Expected any date")
    @Past(message = "Date should be in the past")
    private LocalDate date;
    @NotNull(message = "Expected any category id")
    private int categoryId;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getCategoryId() {
        return (long) categoryId;
    }
}
