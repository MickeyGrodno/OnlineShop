package ru.epam.api.dao;

import ru.epam.models.Comment;

import java.util.List;

public interface CommentDao {
    void save(Comment comment);
    Comment read(int id);
    void update(Comment updatedComment, int id);
    void delete(int id);
    List<Comment> findAll();
    List<Comment> findByUserId(int userId);
}
