package ru.epam.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.epam.dto.UserDto;
import ru.epam.models.User;
import ru.epam.repositories.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }
    public Long getUserIdByLogin(String login) {
        return userRepository.getIdByLogin(login);
    }

    @Override
    public User getUserById(Long id) {
       return userRepository.findById(id).orElse(new User());
    }

    @Override
    public List<User> allUsers() {
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

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

}
