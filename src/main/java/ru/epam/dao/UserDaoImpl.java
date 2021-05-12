package ru.epam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.epam.api.dao.UserDao;
import ru.epam.models.User;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("INSERT INTO user (firstName, lastName, gender, birthDate, login, password, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)", user.getFirstName(), user.getLastName(), user.getGender(),
                user.getBirthDate(), user.getLogin(), user.getPassword(), user.getEmail());
    }

    @Override
    public User read(int id) {
        return jdbcTemplate.query("SELECT * FROM user WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void update(User updatedUser, int id) {
        jdbcTemplate.update("UPDATE user SET firstName=?, lastName=?, gender=?, birthDate=?, login=?, password=?, " +
                        "email=? WHERE id=?", updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getGender(),
                updatedUser.getBirthDate(), updatedUser.getLogin(), updatedUser.getPassword(), updatedUser.getEmail());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM user WHERE id=?", id);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void changeBlockingStatus(User user) {
        jdbcTemplate.update("UPDATE user SET isBlocked=? WHERE id=?", !user.isBlocked(), user.getId());
    }
}
