package com.example.newsapp.controller.request;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {
    @NotBlank(message = "Expected any name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
