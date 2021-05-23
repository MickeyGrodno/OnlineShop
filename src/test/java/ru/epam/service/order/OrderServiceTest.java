package ru.epam.service.order;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.epam.OnlineShopTestRunner;
import ru.epam.models.Order;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.models.User;
import ru.epam.repositories.*;
import ru.epam.service.user.UserProvider;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderServiceTest implements OnlineShopTestRunner {
    @MockBean
    private ProductInCartRepository productInCartRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderInfoRepository orderInfoRepository;
    @MockBean
    private UserProvider userProvider;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private OrderService orderService;


    @Test
    public void saveOrderAndOrderInfo() {
        Long userId = 1L;
        Order order = new Order();
        List<ProductInCart> allUserProducts = new ArrayList<>();
        allUserProducts.add(new ProductInCart(1L, 1L, 1L, 5L));
        allUserProducts.add(new ProductInCart(2L, 2L, 1L, 5L));
        when(productInCartRepository.getAllByUserId(userId)).thenReturn(allUserProducts);
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "name1", 1L, new Date(), 1L, "text", "text"));
        products.add(new Product(2L, "name2", 1L, new Date(), 1L, "text", "text"));
        when(productRepository.findAllById(anyList())).thenReturn(products);
        Order savedOrder = new Order();
        savedOrder.setId(5L);
        when(orderRepository.saveAndFlush(order)).thenReturn(savedOrder);
        Assert.assertEquals(savedOrder.getId(), orderService.saveOrderAndOrderInfo(order));
    }

    @Test
    public void getAllOrders() {
        String login = "name";
        Long userId = 1L;
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order(1L, 1L, 1L, new Date(), true, "text"));
        when(userProvider.getUserName()).thenReturn(login);
        when(userRepository.getIdByLogin(login)).thenReturn(userId);
        when(orderRepository.findAllByUserId(userId)).thenReturn(orderList);
        Assert.assertEquals(orderList.get(0), orderService.getAllOrders().get(0));
    }

    @Test
    public void payOrder_OrderChangeStatus() {
        Long id = 1L;
        Order order = new Order();
        order.setId(id);
        when(orderRepository.findById(id)).thenReturn(java.util.Optional.of(order));
        orderService.payOrder(id);
        ArgumentCaptor<Order> valueCapture = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderRepository, times(1)).saveAndFlush(valueCapture.capture());
        Assert.assertEquals(true, valueCapture.getValue().isHasBeenPaid());
    }

    @Test
    public void getAllOrdersWithUserLogin() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(new Order(1L, 1L, 1L, new Date(), true, "text"));
        allOrders.add(new Order(2L, 1L, 1L, new Date(), true, "text"));
        allOrders.add(new Order(3L, 1L, 1L, new Date(), true, "text"));
        allOrders.add(new Order(4L, 2L, 1L, new Date(), true, "text"));
        allOrders.add(new Order(5L, 2L, 1L, new Date(), true, "text"));
        allOrders.add(new Order(6L, 2L, 1L, new Date(), true, "text"));
        when(orderRepository.findAll()).thenReturn(allOrders);
        Set<Long> usersId = new HashSet<>();
        usersId.add(1L);
        usersId.add(2L);
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "x", "x", "m", new Date(), "login1", "12345", "12345", "ser@mail.ru", "ROLE_USER", 111111111111L));
        users.add(new User(2L, "x", "x", "m", new Date(), "login2", "12345", "12345", "ser@mail.ru", "ROLE_USER", 111111111111L));
        when(userRepository.findAllById(usersId)).thenReturn(users);
        Assert.assertEquals(6, orderService.getAllOrdersWithUserLogin().size());
    }
}
