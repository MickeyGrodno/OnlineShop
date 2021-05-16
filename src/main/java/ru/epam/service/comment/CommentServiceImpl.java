package ru.epam.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.epam.models.Comment;
import ru.epam.repositories.CommentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByProductId(Long id) {
        List<Comment> commentsByProductId = commentRepository.findCommentsByProductId(id);
        commentsByProductId.stream().sorted(Comparator.comparing(Comment::getDate).reversed());
        return commentsByProductId;
    }
}
