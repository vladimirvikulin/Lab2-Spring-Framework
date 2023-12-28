package com.example.newsapp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class News {
    private Long id;
    private String title;
    private String content;
    private LocalDate date;
    private Category category;
}