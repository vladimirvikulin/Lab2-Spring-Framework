package com.example.newsapp.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NewsRequest {
    @NotBlank(message = "Expected any title")
    private String title;
    @NotBlank(message = "Expected any content")
    private String content;
    @NotNull(message = "Expected any date")
    @Past(message = "Date should be in the past")
    private LocalDate date;
    @NotNull(message = "Expected any category id")
    private Long categoryId;
}
