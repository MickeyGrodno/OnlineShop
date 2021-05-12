package ru.epam.api.dao;

import ru.epam.models.User;

import java.util.List;

public interface UserDao {

    void save(User user);
    User read(int id);
    void update(User updatedUser, int id);
    void delete(int id);
    List<User> findAll();
    void changeBlockingStatus(User user);
}
