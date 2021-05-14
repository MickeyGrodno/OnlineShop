package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epam.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
