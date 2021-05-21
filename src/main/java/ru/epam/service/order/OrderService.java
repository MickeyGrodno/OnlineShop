package ru.epam.service.order;

import ru.epam.models.Order;

import java.util.List;

public interface OrderService {
    Long saveOrder(Order order);
    List<Order> getAllOrders();
}
