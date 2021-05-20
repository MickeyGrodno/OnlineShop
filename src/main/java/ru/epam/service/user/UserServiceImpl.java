package ru.epam.service.user;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.epam.config.Roles;
import ru.epam.dto.UserDto;
import ru.epam.models.User;
import ru.epam.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserProvider userProvider;

    public User getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    public Long getUserIdByLogin(String login) {
        return userRepository.getIdByLogin(login);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findUserByLogin(user.getLogin());
        if (userFromDB != null) {
            return false;
        }
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public UserDto getUserDtoByLogin(String login) {
        UserDto userDto = new UserDto();
        User userByLogin = userRepository.getUserByLogin(login);
        userDto.setId(userByLogin.getId());
        userDto.setLogin(userByLogin.getLogin());
        userDto.setFirstName(userByLogin.getFirstName());
        userDto.setLastName(userByLogin.getLastName());
        userDto.setGender(userByLogin.getGender());
        userDto.setEmail(userByLogin.getEmail());
        userDto.setTelephone(userByLogin.getTelephone());
        return userDto;
    }

    public void updateUser(UserDto userDto, String login) {
        User userFromDB = userRepository.findUserByLogin(login);
        userFromDB.setFirstName(userDto.getFirstName());
        userFromDB.setLastName(userDto.getLastName());
        userFromDB.setGender(userDto.getGender());
        userFromDB.setEmail(userDto.getEmail());
        userFromDB.setTelephone(userDto.getTelephone());
        userRepository.save(userFromDB);
    }

    public String getUserRoleById(Long id) {
        return userRepository.getRoleById(id);
    }

    @Override
    @Transactional
    public void updateUserRoleById(Long id, String newUserRoleString) throws NotFoundException {
        String authorizedUserLogin = userProvider.getUsername();

        String authorizedUserRoleString = userRepository
                .getUserByLogin(authorizedUserLogin)
                .getRole();

        Roles authorizedUserRole = Roles.valueOf(authorizedUserRoleString);

        User updatedUser = userRepository.findById(id).orElse(null);
        if (updatedUser!=null) {
            Roles oldUserRole = Roles.valueOf(updatedUser.getRole());
            Roles newUserRole = Roles.valueOf(newUserRoleString);
            if ((authorizedUserRole.equals(Roles.ROLE_SUPERADMIN) && !oldUserRole.equals(Roles.ROLE_SUPERADMIN))) {
                updatedUser.setRole(newUserRoleString);
                userRepository.save(updatedUser);
            } else if ((authorizedUserRole.equals(Roles.ROLE_ADMIN)) && !oldUserRole.equals(Roles.ROLE_ADMIN) &&
                    !oldUserRole.equals(Roles.ROLE_SUPERADMIN) && !newUserRole.equals(Roles.ROLE_SUPERADMIN) &&
                    !newUserRole.equals(Roles.ROLE_ADMIN)) {
                updatedUser.setRole(newUserRoleString);
                userRepository.save(updatedUser);
            }
        } else {
            throw new NotFoundException("Dont found user by Id.");
        }
    }

    @Override
    public boolean updateUserPassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id).get();
        String encodedOldPasswordFromDB = user.getPassword();
        if(passwordEncoder.matches(oldPassword, encodedOldPasswordFromDB)) {

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

}
