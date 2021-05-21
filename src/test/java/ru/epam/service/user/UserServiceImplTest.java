package ru.epam.service.user;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.epam.OnlineShopTestRunner;
import ru.epam.config.Roles;
import ru.epam.models.User;
import ru.epam.repositories.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceImplTest implements OnlineShopTestRunner {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserProvider userProvider;
    @Autowired
    private UserService userService;

    @Test
    public void updateUserRoleById_SuperAdminSetRollAdmin() throws NotFoundException {
        ru.epam.models.User user = new User();
        user.setRole(Roles.ROLE_SUPERADMIN.name());
        when(userProvider.getUsername()).thenReturn("");
        when(userRepository.getUserByLogin(any(String.class))).thenReturn(user);
        ru.epam.models.User updatedUser = new User();
        updatedUser.setRole(Roles.ROLE_USER.name());
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(updatedUser));
        userService.updateUserRoleById(1L, Roles.ROLE_ADMIN.name());
        Mockito.verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void updateUserRoleById_AdminSetRollBlockedToUser() throws NotFoundException {
        ru.epam.models.User user = new User();
        user.setRole(Roles.ROLE_ADMIN.name());
        when(userProvider.getUsername()).thenReturn("");
        when(userRepository.getUserByLogin(any(String.class))).thenReturn(user);
        ru.epam.models.User updatedUser = new User();
        updatedUser.setRole(Roles.ROLE_USER.name());
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(updatedUser));
        userService.updateUserRoleById(1L, Roles.ROLE_BLOCKED.name());
        Mockito.verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void updateUserRoleById_AdminSetRollBlockedToAdmin() throws NotFoundException {
        ru.epam.models.User user = new User();
        user.setRole(Roles.ROLE_ADMIN.name());
        when(userProvider.getUsername()).thenReturn("");
        when(userRepository.getUserByLogin(any(String.class))).thenReturn(user);
        ru.epam.models.User updatedUser = new User();
        updatedUser.setRole(Roles.ROLE_ADMIN.name());
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(updatedUser));
        userService.updateUserRoleById(1L, Roles.ROLE_BLOCKED.name());
        Mockito.verify(userRepository, times(0)).save(updatedUser);
    }
}