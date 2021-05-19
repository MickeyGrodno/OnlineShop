package ru.epam.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UserDto {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthDate;
    private String email;
    private Long telephone;
}