package ru.epam.dto;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CommentDto {
    private Long id;
    private String message;
    private Long productId;
    private String login;
    private LocalDateTime date;
}
