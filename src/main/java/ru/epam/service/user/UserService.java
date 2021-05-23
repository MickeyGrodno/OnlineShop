package ru.epam.service.user;

import javassist.NotFoundException;
import ru.epam.dto.UserDto;
import ru.epam.models.User;

public interface UserService {

    boolean saveUser(User user);

    void updateUser(UserDto userDto, String login);

    UserDto getUserDtoByLogin(String login);

    void updateUserRoleById(Long id, String newRole) throws NotFoundException;

    void deleteUserById(Long id);

    boolean updateUserPassword(Long id, String oldPassword, String newPassword);
}
