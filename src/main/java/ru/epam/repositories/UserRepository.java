package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.epam.models.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);
    @Query(nativeQuery = true, value="SELECT login FROM user WHERE id=:myId")
    String findUserLoginById(@Param(value = "myId") Long id);
}
