package ru.epam.service.comment;

import ru.epam.dto.CommentDto;
import ru.epam.models.Comment;

import java.util.List;

public interface CommentService {
    void saveComment(Comment comment);
    List<CommentDto> getCommentsDtoByProductId(Long id);
}
