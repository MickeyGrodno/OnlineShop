package ru.epam.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String message;
    private Long productId;
    private String login;
    private LocalDateTime date;
}
