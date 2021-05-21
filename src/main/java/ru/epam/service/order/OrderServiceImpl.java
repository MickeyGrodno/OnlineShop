package ru.epam.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epam.models.Order;
import ru.epam.models.OrderInfo;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.repositories.*;
import ru.epam.service.user.UserProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
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

        allOrderInfo.forEach(a -> a.setOrderId(savedOrder.getId()));

        orderInfoRepository.saveAll(allOrderInfo);
        productInCartRepository.removeAllByUserId(order.getUserId());
        return savedOrder.getId();
    }

    @Override
    public List<Order> getAllOrders() {
        String login = userProvider.getUserName();
        Long userId = userRepository.getIdByLogin(login);
        return orderRepository.findAllByUserId(userId);
    }

    @Transactional
    @Override
    public void payOrder(Long id) {
       Order order = orderRepository.findById(id).orElse(null);
       order.setHasBeenPaid(true);
       orderRepository.saveAndFlush(order);
    }
}
