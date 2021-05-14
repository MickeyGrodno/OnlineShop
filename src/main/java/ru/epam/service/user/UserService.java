package ru.epam.service.user;

import ru.epam.models.User;

public interface UserService {
    public User loadUserByLogin(String login);
}
