package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epam.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByProductId(Long id);

    void deleteAllByProductId(Long productId);

    void deleteAllByUserId(Long userId);
}
