package ru.epam.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserDto {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String email;
    private Long telephone;
}
