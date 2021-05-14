package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epam.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);
}
