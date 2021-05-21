package ru.epam.service.order;

import ru.epam.models.Order;

import java.util.List;

public interface OrderService {
    Long saveOrderAndOrderInfo(Order order);
    List<Order> getAllOrders();
    void payOrder(Long id);
}
