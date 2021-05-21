package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.epam.models.User;

public interface UserRepository extends JpaRepository<User, Long>  {
    User findUserByLogin(String login);

    @Query(nativeQuery = true, value = "SELECT id FROM users WHERE login=:login")
    Long getIdByLogin(@Param(value = "login") String login);

    User getUserByLogin(String login);

//    @Modifying
//    @Query(nativeQuery = true, value = "update users set role=:role WHERE id=:id")
//    void updateUserRoleById(@Param(value = "id")Long id, @Param(value = "role")String role);
//
//    @Query(nativeQuery = true, value = "SELECT role FROM users WHERE id=:id")
//    String getRoleById(@Param(value = "id") Long id);
//
//    @Query(nativeQuery = true, value = "SELECT password FROM users WHERE id=:id")
//    String getPasswordById(@Param(value = "id") Long id);
}
