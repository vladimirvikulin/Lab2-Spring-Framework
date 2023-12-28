package com.example.newsapp.repository;

import com.example.newsapp.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CategoryRepository implements CategoryDAO {
    private final JdbcTemplate jdbcTemplate;

    private static class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(rs.getLong("id"));
            category.setName(rs.getString("name"));
            return category;
        }
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT * FROM category";
        return jdbcTemplate.query(sql, new CategoryRowMapper());
    }

    @Override
    public Optional<Category> findById(Long id) {
        String sql = "SELECT * FROM category WHERE id = ?";
        List<Category> result = jdbcTemplate.query(sql, new CategoryRowMapper(), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Category> findByName(String keyword) {
        String sql = "SELECT * FROM category WHERE name LIKE ?";
        return jdbcTemplate.query(sql, new CategoryRowMapper(), "%" + keyword + "%");
    }

    @Override
    public Long save(Category category) {
        String sql = "INSERT INTO category(name) VALUES (?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Long.class, category.getName());
    }

    @Override
    public boolean update(Category category) {
        String sql = "UPDATE category SET name = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, category.getName(), category.getId());
        return rowsUpdated > 0;
    }

    @Override
    public boolean delete(Category category) {
        String sql = "DELETE FROM category WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, category.getId());
        return rowsUpdated > 0;
    }
}
