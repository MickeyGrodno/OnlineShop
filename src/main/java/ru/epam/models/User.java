package ru.epam.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private Date birthDate;
    @Size(min=2, message = "Не меньше 5 знаков")
    @Column(name = "login")
    private String login;
    @Size(min=1, message = "Не меньше 5 знаков")
    @Column(name = "password")
    private String password;
    @Transient
    private String passwordConfirm;
    @Column(name = "email")
    private String email;
    @Column(name = "role")
    private String role;
    @Column(name = "telephone")
    private Long telephone;
}