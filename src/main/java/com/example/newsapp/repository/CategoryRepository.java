package com.example.newsapp.repository;

import com.example.newsapp.model.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Modifying
    @Query("UPDATE Category c SET c.name = :newName WHERE c.id = :id")
    int update(@Param("newName") String newName, @Param("id") Long id);
}
