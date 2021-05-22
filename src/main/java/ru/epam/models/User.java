package ru.epam.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    @Size(min=2, max=30, message = "Длинна логина должна быть от 2 до 30 символов!")
    @NotEmpty(message = "Поле 'Логин' не может быть пустым!")
    @Column(name = "login")
    private String login;
    @Size(min=5, message = "Длинна пароля не может быть меньше 5 символов!")
    @Column(name = "password")
    private String password;
    @Transient
    private String passwordConfirm;
    @NotEmpty(message = "Поле email не может быть пустым!")
    @Email(message = "Введен некорректный email адрес!")
    @Column(name = "email")
    private String email;
    @Column(name = "role")
    private String role;
    @Column(name = "telephone")
    private Long telephone;
}