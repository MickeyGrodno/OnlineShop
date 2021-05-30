package ru.epam.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epam.dto.OrderDto;
import ru.epam.models.*;
import ru.epam.repositories.*;
import ru.epam.service.user.UserProvider;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserProvider userProvider;
    private final UserRepository userRepository;
    private final ProductInCartRepository productInCartRepository;
    private final ProductRepository productRepository;
    private final OrderInfoRepository orderInfoRepository;

    @Transactional
    @Override
    public Long saveOrderAndOrderInfo(Order order) {
        Date date = new Date();
        order.setDate(date);

        List<ProductInCart> allUserProducts = productInCartRepository.getAllByUserId(order.getUserId());
        List<Long> productsId = allUserProducts.stream().map(ProductInCart::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productsId);
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, product -> product));
        List<OrderInfo> allOrderInfo = new ArrayList<>();

        for (ProductInCart productInCart : allUserProducts) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setProductCount(productInCart.getProductCount());
            Product product = productMap.get(productInCart.getProductId());
            orderInfo.setProductName(product.getName());
            orderInfo.setPrice(product.getPrice());
            orderInfo.setTotalPrice(orderInfo.getProductCount() * orderInfo.getPrice());
            allOrderInfo.add(orderInfo);
        }
        Order savedOrder = orderRepository.saveAndFlush(order);
        log.info("Order #{} have been successfully saved.", savedOrder.getId());
        allOrderInfo.forEach(a -> a.setOrderId(savedOrder.getId()));

        orderInfoRepository.saveAll(allOrderInfo);
        log.info("Successfully saved all orderInfo order #" + savedOrder.getId());
        productInCartRepository.removeAllByUserId(order.getUserId());
        log.info("Successfully emptied the shopping cart of user #" + order.getUserId());
        return savedOrder.getId();
    }

    @Override
    public List<Order> getAllOrders() {
        String login = userProvider.getUserName();
        Long userId = userRepository.getIdByLogin(login);
        List<Order> allSortedOrders = orderRepository.findAllByUserId(userId)
                .stream()
                .sorted(Comparator
                        .comparing(Order::getDate)
                        .reversed())
                .collect(Collectors.toList());
        log.info("User № {} orders uploaded successfully", userId);
        return allSortedOrders;
    }

    @Transactional
    @Override
    public void payOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        order.setHasBeenPaid(true);
        log.info("Order # {} status has been changed to \"Paid\".", order.getId());
        orderRepository.saveAndFlush(order);
        log.info("Order # {} is saved.");
    }

    public List<OrderDto> getAllOrdersWithUserLogin() {
        List<Order> allOrders = orderRepository.findAll();
        log.info("All orders have been loaded.");
        Set<Long> usersId = allOrders.stream().map(Order::getUserId).collect(Collectors.toSet());
        List<User> users = userRepository.findAllById(usersId);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        List<OrderDto> allOrdersWithUserLogin = new ArrayList<>();

        for (Order order : allOrders) {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getUserId());
            orderDto.setPrice(order.getPrice());
            orderDto.setDate(order.getDate());
            orderDto.setHasBennPaid(order.isHasBeenPaid());
            orderDto.setAddress(order.getAddress());
            orderDto.setUserLogin(userMap
                    .get(order.getUserId())
                    .getLogin());
            allOrdersWithUserLogin.add(orderDto);
        }
        allOrdersWithUserLogin.sort(Comparator.comparing(OrderDto::getDate));
        log.info("Received all orderDto and sorted by creation date.");
        return allOrdersWithUserLogin;
    }
}
