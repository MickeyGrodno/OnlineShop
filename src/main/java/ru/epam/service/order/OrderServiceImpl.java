package ru.epam.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.epam.models.Order;
import ru.epam.repositories.OrderRepository;
import ru.epam.repositories.UserRepository;
import ru.epam.service.user.UserProvider;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final UserProvider userProvider;
    private final UserRepository userRepository;

    @Override
    public Long saveOrder(Order order) {
        Date date = new Date();
        order.setDate(date);
        Order savedOrder = orderRepository.saveAndFlush(order);
        return savedOrder.getId();
    }

    @Override
    public List<Order> getAllOrders() {
        String login = userProvider.getUsername();
        Long userId = userRepository.getIdByLogin(login);
        return orderRepository.findAllByUserId(userId);
    }
}
