package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.epam.models.User;

public interface UserRepository extends JpaRepository<User, Long>  {
    User findUserByLogin(String login);

    @Query(nativeQuery = true, value = "SELECT id FROM users WHERE login=:login")
    Long getIdByLogin(@Param(value = "login") String login);
}
