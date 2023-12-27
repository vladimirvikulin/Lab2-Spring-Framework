package com.example.newsapp.repository;

import com.example.newsapp.model.Category;
import com.example.newsapp.model.News;
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
public class NewsRepository implements NewsDAO {
    private final JdbcTemplate jdbcTemplate;
    private final CategoryRepository categoryDAO;

    private static class NewsRowMapper implements RowMapper<News> {
        CategoryRepository categoryDAO;

        public NewsRowMapper(CategoryRepository categoryDAO) {
            this.categoryDAO = categoryDAO;
        }
        @Override
        public News mapRow(ResultSet rs, int rowNum) throws SQLException {
            News news = new News();
            news.setId(rs.getLong("id"));
            news.setTitle(rs.getString("title"));
            news.setContent(rs.getString("content"));
            news.setDate(rs.getDate("date").toLocalDate());

            Optional<Category> category = categoryDAO.findById(rs.getLong("category_id"));
            category.ifPresent(news::setCategory);

            return news;
        }
    }

    @Override
    public List<News> findAll() {
        String sql = "SELECT * FROM news";
        return jdbcTemplate.query(sql, new NewsRowMapper(categoryDAO));
    }

    @Override
    public Optional<News> findById(Long id) {
        String sql = "SELECT * FROM news WHERE id = ?";
        List<News> result = jdbcTemplate.query(sql, new NewsRowMapper(categoryDAO), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<News> findByCategory(Category category) {
        String sql = "SELECT * FROM news WHERE category_id = ?";
        return jdbcTemplate.query(sql, new NewsRowMapper(categoryDAO), category.getId());
    }

    @Override
    public List<News> findByTitleContaining(String keyword) {
        String sql = "SELECT * FROM news WHERE title LIKE ?";
        return jdbcTemplate.query(sql, new NewsRowMapper(categoryDAO), "%" + keyword + "%");
    }

    @Override
    public Long save(News news) {
        String sql = "INSERT INTO news(title, content, date, category_id) " +
                "VALUES (?, ?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(
                sql, Long.class,
                news.getTitle(),
                news.getContent(),
                news.getDate(),
                news.getCategory().getId()
        );
    }

    @Override
    public boolean update(News news) {
        String sql = "UPDATE news SET title = ?, content = ?, date = ?, category_id = ? WHERE id = ?";

        int rowsUpdated = jdbcTemplate.update(
                sql,
                news.getTitle(),
                news.getContent(),
                news.getDate(),
                news.getCategory().getId(),
                news.getId()
        );

        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteAll(List<News> news) {
        String sql = "DELETE FROM news WHERE id IN (?)";
        Object[] ids = news.stream().map(News::getId).toArray();
        if (ids.length == 0) {
            return true;
        }
        int rowsUpdated = jdbcTemplate.update(sql, ids);
        return rowsUpdated > 0;
    }

    @Override
    public boolean delete(News news) {
        String sql = "DELETE FROM news WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, news.getId());
        return rowsUpdated > 0;
    }
}
