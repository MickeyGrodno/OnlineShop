package ru.epam.service.order;

import ru.epam.models.Order;

public interface OrderService {
    Long saveOrder(Order order);
}
