package ru.epam.service.user;

import ru.epam.dto.UserDto;
import ru.epam.models.User;

import java.util.List;

public interface UserService {
    User getUserByLogin(String login);
    Long getUserIdByLogin(String login);
    User getUserById(Long id);
    public List<User> allUsers();
    boolean saveUser(User user);
    void updateUser(UserDto userDto, String login);
    UserDto getUserDtoByLogin(String login);
}
