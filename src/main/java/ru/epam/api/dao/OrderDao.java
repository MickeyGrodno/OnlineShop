package ru.epam.api.dao;

import ru.epam.models.Order;

import java.util.List;

public interface OrderDao {
    void save(Order order);
    Order read(int id);
    void update(Order updatedOrder, int id);
    void delete(int id);
    List<Order> findAll();
    List<Order> findByUserId(int userId);
    void changePaymentStatus(Order order);
}
