package ru.epam.service.user;

import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ru.epam.OnlineShopTestRunner;
import ru.epam.config.Roles;
import ru.epam.dto.UserDto;
import ru.epam.models.Order;
import ru.epam.models.User;
import ru.epam.repositories.CommentRepository;
import ru.epam.repositories.OrderInfoRepository;
import ru.epam.repositories.OrderRepository;
import ru.epam.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceImplTest implements OnlineShopTestRunner {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserProvider userProvider;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private User user;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private OrderInfoRepository orderInfoRepository;
    @Autowired
    private UserService userService;

    @Test
    public void updateUserRoleById_SuperAdminSetRollAdmin() throws NotFoundException {
        ru.epam.models.User user = new User();
        user.setRole(Roles.ROLE_SUPERADMIN.name());
        when(userProvider.getUserName()).thenReturn("");
        when(userRepository.getUserByLogin(any(String.class))).thenReturn(user);
        ru.epam.models.User updatedUser = new User();
        updatedUser.setRole(Roles.ROLE_USER.name());
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(updatedUser));
        userService.updateUserRoleById(1L, Roles.ROLE_ADMIN.name());
        Mockito.verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void updateUserRoleById_AdminSetRollBlockedToUser() throws NotFoundException {
        user = new User();
        user.setRole(Roles.ROLE_ADMIN.name());
        when(userProvider.getUserName()).thenReturn("");
        when(userRepository.getUserByLogin(any(String.class))).thenReturn(user);
        ru.epam.models.User updatedUser = new User();
        updatedUser.setRole(Roles.ROLE_USER.name());
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(updatedUser));
        userService.updateUserRoleById(1L, Roles.ROLE_BLOCKED.name());
        Mockito.verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void updateUserRoleById_AdminSetRollBlockedToAdmin() throws NotFoundException {
        user = new User();
        user.setRole(Roles.ROLE_ADMIN.name());
        when(userProvider.getUserName()).thenReturn("");
        when(userRepository.getUserByLogin(any(String.class))).thenReturn(user);
        ru.epam.models.User updatedUser = new User();
        updatedUser.setRole(Roles.ROLE_ADMIN.name());
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(updatedUser));
        userService.updateUserRoleById(1L, Roles.ROLE_BLOCKED.name());
        Mockito.verify(userRepository, never()).save(updatedUser);
    }

    @Test
    public void deleteUserById_UserDeleted() {
        user = new User();
        user.setRole(Roles.ROLE_ADMIN.name());
        when(userProvider.getUserName()).thenReturn("");
        when(userRepository.getUserByLogin(any(String.class))).thenReturn(user);
        List<Order> orders = new ArrayList<>();
        when(orderRepository.findAllByUserId(anyLong())).thenReturn(orders);
        userService.deleteUserById(1L, "ROLE_USER");
        Mockito.verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void saveUser_UserFound() {
        user = new User();
        user.setLogin("log");
        User userFromDB = new User();
        when(userRepository.getUserByLogin(user.getLogin())).thenReturn(userFromDB);
        Assert.assertFalse(userService.saveUser(user));
    }

    @Test
    public void saveUser_UserNotFound() {
        when(userRepository.getUserByLogin(user.getLogin())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("pass");
        userRepository.save(user);
        Assert.assertTrue(userService.saveUser(user));
    }

    @Test
    public void getUserDtoByLogin_UserConvertedToUserDto() {
        User userFromDB = new User();
        userFromDB.setEmail("email");
        when(userRepository.getUserByLogin(anyString())).thenReturn(userFromDB);
        Assert.assertEquals("email", userService.getUserDtoByLogin("login").getEmail());
    }

    @Test
    public void updateUser_UpdateAndSaveUser() {
        User userFromDB = new User();
        UserDto userDto = new UserDto();
        userFromDB.setEmail("email");
        when(userRepository.getUserByLogin(anyString())).thenReturn(userFromDB);
        userService.updateUser(userDto, "login");
        Mockito.verify(userRepository, times(1)).save(userFromDB);
    }

    @Test
    public void updateUserPassword_PasswordSuccessfulUpdate() {
        User userFromDb = new User();
        userFromDb.setPassword("123");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userFromDb));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("pass");
        Assert.assertTrue(userService.updateUserPassword(1L, "string", "string"));
    }

    @Test
    public void updateUserPassword_PasswordUnsuccessfulUpdate() {
        User userFromDb = new User();
        userFromDb.setPassword("123");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userFromDb));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        Assert.assertFalse(userService.updateUserPassword(1L, "string", "string"));
    }


}