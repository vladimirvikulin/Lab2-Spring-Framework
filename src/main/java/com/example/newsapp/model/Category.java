package com.example.newsapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@NamedQuery(name = "Category.findByName", query = "SELECT c FROM Category c WHERE c.name = :name")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
