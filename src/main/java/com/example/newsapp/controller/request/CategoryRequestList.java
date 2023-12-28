package com.example.newsapp.controller.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestList {
    @Valid
    private CategoryRequest[] categories;
}
