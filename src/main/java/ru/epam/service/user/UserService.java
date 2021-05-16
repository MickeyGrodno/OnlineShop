package ru.epam.service.user;

import ru.epam.models.User;

import java.util.List;

public interface UserService {
    User loadUserByLogin(String login);
}
