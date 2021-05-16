package ru.epam.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.epam.models.Order;
import ru.epam.repositories.OrderRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    @Override
    public Long saveOrder(Order order) {
        Date date = new Date();
        order.setDate(date);
        Order savedOrder = orderRepository.saveAndFlush(order);
        return savedOrder.getId();
    }
}
