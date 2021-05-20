package ru.epam.service.user;

import javassist.NotFoundException;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.epam.OnlineShopTestRunner;
import ru.epam.models.User;
import ru.epam.repositories.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;


public class UserServiceTest implements OnlineShopTestRunner {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserProvider userProvider;
    @Autowired
    private UserService userService;

    @Test
    public void updateUserRoleById_UserRole() throws NotFoundException {


        ru.epam.models.User user = new User();
        user.setRole("ROLE_ADMIN");
        given(userProvider.getUsername()).willReturn(any(String.class));
        given(userRepository.getUserByLogin(any(String.class))).willReturn(user);
        ru.epam.models.User updatedUser = new User();
        updatedUser.setRole("ROLE_USER");
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(updatedUser));
        given(userRepository.save(any())).willReturn(any());

        userService.updateUserRoleById(any(Long.class), "ROLE_ADMIN");
        Mockito.when(userRepository.count()).thenReturn(1L);
        Mockito.verify(userRepository, times(1)).save(any());

    }
}
