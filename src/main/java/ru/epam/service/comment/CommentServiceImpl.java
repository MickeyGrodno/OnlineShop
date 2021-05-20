package ru.epam.service.comment;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.epam.models.Comment;
import ru.epam.repositories.CommentRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByProductId(Long id) {
        List<Comment> commentsByProductId = commentRepository.findCommentsByProductId(id);
        commentsByProductId.sort(Comparator.comparing(Comment::getDate).reversed());
        return commentsByProductId;
    }

    public void saveComment(Comment comment) {
        comment.setDate(new Date());
        commentRepository.save(comment);
    }
}
