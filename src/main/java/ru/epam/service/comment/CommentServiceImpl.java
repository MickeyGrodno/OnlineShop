package ru.epam.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.epam.dto.CommentDto;
import ru.epam.models.Comment;
import ru.epam.models.User;
import ru.epam.repositories.CommentRepository;
import ru.epam.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public void saveComment(Comment comment) {
        comment.setDate(LocalDateTime.now());
        commentRepository.save(comment);
        log.info("The comment was saved in the database.");
    }

    public List<CommentDto> getCommentsDtoByProductId(Long id) {
        List<Comment> allComments = commentRepository.findCommentsByProductId(id);
        List<Long> usersId = allComments.stream().map(Comment::getUserId).collect(Collectors.toList());
        List<User> users = userRepository.findAllById(usersId);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        List<CommentDto> resultDtos = new ArrayList<>();

        for (Comment comment : allComments) {
            CommentDto dto = new CommentDto();
            dto.setId(comment.getId());
            dto.setMessage(comment.getMessage());
            dto.setProductId(comment.getProductId());
            dto.setDate(comment.getDate());
            User user = userMap.get(comment.getUserId());
            dto.setLogin(user.getLogin());
            resultDtos.add(dto);
        }
        resultDtos.sort(Comparator.comparing(CommentDto::getDate).reversed());
        log.info("Comments have been successfully converted to commentDto and sorted.");
        return resultDtos;
    }

}
