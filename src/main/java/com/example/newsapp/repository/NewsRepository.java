package com.example.newsapp.repository;

import com.example.newsapp.model.News;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NewsRepository extends CrudRepository<News, Long> {
    @Query("SELECT n FROM News n WHERE n.category.id = :id")
    List<News> findByCategory(@Param("id") Long categoryId);

    List<News> findByTitleContaining(String keyword);

    @Modifying
    @Query("UPDATE News n SET n.title = :newTitle, n.content = :newContent, n.date = :newDate, n.category.id = :newCategoryId WHERE n.id = :id")
    int update(
            @Param("newTitle") String newTitle,
            @Param("newContent") String newContent,
            @Param("newDate") LocalDate newDate,
            @Param("newCategoryId") Long newCategoryId,
            @Param("id") Long id
    );
}