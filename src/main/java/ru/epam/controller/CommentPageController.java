package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.epam.models.Comment;
import ru.epam.service.comment.CommentService;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentPageController {
    private final CommentService commentService;

    @PostMapping("/post")
    public String changePassword(@RequestParam("userId") Long userId,
                                 @RequestParam("productId") Long productId,
                                 Comment comment) {
        comment.setUserId(userId);
        comment.setProductId(productId);
        commentService.saveComment(comment);
        return "redirect:/product/" + comment.getProductId();

    }
}
