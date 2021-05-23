package ru.epam.service.comment;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.epam.OnlineShopTestRunner;
import ru.epam.dto.CommentDto;
import ru.epam.models.Comment;
import ru.epam.models.User;
import ru.epam.repositories.CommentRepository;
import ru.epam.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CommentServiceTest implements OnlineShopTestRunner {
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private CommentService commentService;

    @Test
    public void saveComment_DatePresent() {
        Comment comment = new Comment();
        commentService.saveComment(comment);
        ArgumentCaptor<Comment> valueCapture = ArgumentCaptor.forClass(Comment.class);
        Mockito.verify(commentRepository, times(1)).save(valueCapture.capture());
        Assert.assertNotNull(valueCapture.getValue().getDate());
    }

    @Test
    public void getCommentsDtoByProductId() {
        Long productId = 1L;
        List<Comment> allComments = new ArrayList<>();
        allComments.add(new Comment(1L, LocalDateTime.now(), "text", productId, 1L));
        allComments.add(new Comment(2L, LocalDateTime.now(), "text", productId, 2L));

        when(commentRepository.findCommentsByProductId(productId)).thenReturn(allComments);

        List<Long> usersId = new ArrayList<>();
        usersId.add(1L);
        usersId.add(2L);

        List<User> users = new ArrayList<>();
        users.add(new User(1L, "x", "x", "m", new Date(), "login1", "12345", "12345", "ser@mail.ru", "ROLE_USER", 111111111111L));
        users.add(new User(2L, "x", "x", "m", new Date(), "login2", "12345", "12345", "ser@mail.ru", "ROLE_USER", 111111111111L));

        when(userRepository.findAllById(usersId)).thenReturn(users);

        List<CommentDto> commentDto = commentService.getCommentsDtoByProductId(productId);
        Assert.assertEquals(2, commentDto.size());
    }
}
