package ru.epam.service.comment;

import ru.epam.models.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByProductId(Long id);
}
