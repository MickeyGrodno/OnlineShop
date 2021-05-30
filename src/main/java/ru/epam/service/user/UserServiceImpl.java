package ru.epam.service.user;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.epam.config.Roles;
import ru.epam.dto.UserDto;
import ru.epam.models.Order;
import ru.epam.models.User;
import ru.epam.repositories.CommentRepository;
import ru.epam.repositories.OrderInfoRepository;
import ru.epam.repositories.OrderRepository;
import ru.epam.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserProvider userProvider;
    private final OrderRepository orderRepository;
    private final CommentRepository commentRepository;
    private final OrderInfoRepository orderInfoRepository;

    public boolean saveUser(User user) {
        User userFromDB = userRepository.getUserByLogin(user.getLogin());
        if (userFromDB != null) {
            log.info("User with login '{}' loaded", userFromDB);
            return false;
        }
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("User saved");

        return true;
    }

    public UserDto getUserDtoByLogin(String login) {
        UserDto userDto = new UserDto();
        User userByLogin = userRepository.getUserByLogin(login);
        log.info("User with login '{}' loaded", login);
        userDto.setId(userByLogin.getId());
        userDto.setLogin(userByLogin.getLogin());
        userDto.setFirstName(userByLogin.getFirstName());
        userDto.setLastName(userByLogin.getLastName());
        userDto.setGender(userByLogin.getGender());
        userDto.setEmail(userByLogin.getEmail());
        userDto.setTelephone(userByLogin.getTelephone());
        userDto.setBirthDate(userByLogin.getBirthDate());
        return userDto;
    }

    public void updateUser(UserDto userDto, String login) {
        User userFromDB = userRepository.getUserByLogin(login);
        log.info("User with login '{}' loaded", login);
        userFromDB.setFirstName(userDto.getFirstName());
        userFromDB.setLastName(userDto.getLastName());
        userFromDB.setGender(userDto.getGender());
        userFromDB.setEmail(userDto.getEmail());
        userFromDB.setTelephone(userDto.getTelephone());
        userFromDB.setBirthDate(userDto.getBirthDate());
        userRepository.save(userFromDB);
        log.info("User with login '{}' updated", login);
    }

    @Override
    @Transactional
    public void updateUserRoleById(Long id, String newUserRoleString) throws NotFoundException {
        String authorizedUserLogin = userProvider.getUserName();

        String authorizedUserRoleString = userRepository
                .getUserByLogin(authorizedUserLogin)
                .getRole();
        log.info("User's role has been loaded");

        Roles authorizedUserRole = Roles.valueOf(authorizedUserRoleString);

        User updatedUser = userRepository.findById(id).orElse(null);
        if (updatedUser != null) {
            Roles oldUserRole = Roles.valueOf(updatedUser.getRole());
            Roles newUserRole = Roles.valueOf(newUserRoleString);
            if (((authorizedUserRole.equals(Roles.ROLE_SUPERADMIN) && !oldUserRole.equals(Roles.ROLE_SUPERADMIN)))
            || ((authorizedUserRole.equals(Roles.ROLE_ADMIN)) && !oldUserRole.equals(Roles.ROLE_ADMIN) &&
                    !oldUserRole.equals(Roles.ROLE_SUPERADMIN) && !newUserRole.equals(Roles.ROLE_SUPERADMIN) &&
                    !newUserRole.equals(Roles.ROLE_ADMIN))) {
                updatedUser.setRole(newUserRoleString);
                userRepository.save(updatedUser);
                log.info("User's role has been updated.");
            }
        } else {
            throw new NotFoundException("Dont found user by Id.");
        }
    }

    @Override
    public boolean updateUserPassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id).get();
        String encodedOldPasswordFromDB = user.getPassword();
        if (passwordEncoder.matches(oldPassword, encodedOldPasswordFromDB)) {

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            log.info("User's password has benn updated.");
            return true;
        } else {
            log.info("User's password mismatch");
            return false;
        }
    }

    @Override
    @Transactional
    public void deleteUserById(Long id, String userRole) {
        String authorizedUserLogin = userProvider.getUserName();
        String authorizedUserRoleString = userRepository
                .getUserByLogin(authorizedUserLogin)
                .getRole();
        log.info("User's role has been loaded");

        Roles authorizedUserRole = Roles.valueOf(authorizedUserRoleString);
        Roles deletedUserRole = Roles.valueOf(userRole);
            if (((authorizedUserRole.equals(Roles.ROLE_SUPERADMIN) && !deletedUserRole.equals(Roles.ROLE_SUPERADMIN)))
                    || ((authorizedUserRole.equals(Roles.ROLE_ADMIN)) && !deletedUserRole.equals(Roles.ROLE_ADMIN) &&
                    !deletedUserRole.equals(Roles.ROLE_SUPERADMIN))) {
                List<Order> allUserOrders = orderRepository.findAllByUserId(id);
                log.info("User # {} orders loaded", id);
                List<Long> allDeletedOrderId = allUserOrders.stream().map(Order::getId).collect(Collectors.toList());
                commentRepository.deleteAllByUserId(id);
                log.info("User #  {} comments removed", id);
                orderInfoRepository.deleteAllByOrderIdIn(allDeletedOrderId);
                log.info("User #  {} orders info removed", id);
                orderRepository.deleteAllByUserId(id);
                log.info("User #  {} orders removed", id);
                userRepository.deleteById(id);
                log.info("User #  {} removed", id);
            }




//        List<Order> allUserOrders = orderRepository.findAllByUserId(id);
//        log.info("User # {} orders loaded", id);
//        List<Long> allDeletedOrderId = allUserOrders.stream().map(Order::getId).collect(Collectors.toList());
//        commentRepository.deleteAllByUserId(id);
//        log.info("User #  {} comments removed", id);
//        orderInfoRepository.deleteAllByOrderIdIn(allDeletedOrderId);
//        log.info("User #  {} orders info removed", id);
//        orderRepository.deleteAllByUserId(id);
//        log.info("User #  {} orders removed", id);
//        userRepository.deleteById(id);
//        log.info("User #  {} removed", id);
    }
}
