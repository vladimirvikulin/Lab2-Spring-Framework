package com.example.newsapp.controller.request;

public class NewsRequest {
    private String title;
    private String content;
    private String date;
    private String categoryId;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
