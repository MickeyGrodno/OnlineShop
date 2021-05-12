package ru.epam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.epam.api.dao.OrderDao;
import ru.epam.models.Order;

import java.util.List;

@Component
public class OrderDaoImpl implements OrderDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Order order) {
        jdbcTemplate.update("INSERT INTO order (userId, price, date, hasBeenPaid, address)" +
                        "VALUES (?, ?, ?, ?, ?)", order.getUserId(), order.getPrice(),
                order.getDate(), order.isHasBeenPaid(), order.getAddress());
    }

    @Override
    public Order read(int id) {
        return jdbcTemplate.query("SELECT * FROM order WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Order.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void update(Order updatedOrder, int id) {
        jdbcTemplate.update("UPDATE order SET userId=?, price=?, date=?, hasBeenPaid=?, address=? " +
                        "WHERE id=?", updatedOrder.getUserId(),updatedOrder.getPrice(),
                updatedOrder.getDate(), updatedOrder.isHasBeenPaid(), updatedOrder.getAddress());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM order WHERE id=?", id);
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query("SELECT * FROM order", new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public List<Order> findByUserId(int userId) {
        return jdbcTemplate.query("SELECT * FROM order WHERE id=?", new Object[]{userId}, new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public void changePaymentStatus(Order order) {
        jdbcTemplate.update("UPDATE user SET isBlocked=? WHERE id=?",
                !order.isHasBeenPaid(), order.getId());
    }
}
