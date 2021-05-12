package ru.epam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.epam.api.dao.CommentDao;
import ru.epam.models.Comment;

import java.util.List;

@Component
public class CommentDaoImpl implements CommentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Comment comment) {
        jdbcTemplate.update("INSERT INTO comment (date, message, productId, userId)" +
                        "VALUES (?, ?, ?, ?)", comment.getDate(), comment.getMessage(), comment.getProductId(),
                comment.getUserId());
    }

    @Override
    public Comment read(int id) {
        return jdbcTemplate.query("SELECT * FROM comment WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Comment.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void update(Comment updatedComment, int id) {
            jdbcTemplate.update("UPDATE comment SET date=?, message=?, productId=?, userId=? " +
                    "WHERE id=?", updatedComment.getDate(),updatedComment.getMessage(),
                    updatedComment.getProductId(), updatedComment.getUserId(), id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM comment WHERE id=?", id);
    }

    @Override
    public List<Comment> findAll() {
        return jdbcTemplate.query("SELECT * FROM comment", new BeanPropertyRowMapper<>(Comment.class));
    }

    @Override
    public List<Comment> findByUserId(int userId) {
        return jdbcTemplate.query("SELECT * FROM comment WHERE id=?", new Object[]{userId},
                new BeanPropertyRowMapper<>(Comment.class));
    }
}
