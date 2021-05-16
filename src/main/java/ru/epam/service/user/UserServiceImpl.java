package ru.epam.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.epam.models.User;
import ru.epam.repositories.UserRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public User loadUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }
    public Long getUserIdByLogin(String login) {
        return userRepository.getIdByLogin(login);
    }
}
