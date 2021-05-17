package ru.epam.service.user;

import ru.epam.models.User;

public interface UserService {
    User loadUserByLogin(String login);
    Long getUserIdByLogin(String login);
}
