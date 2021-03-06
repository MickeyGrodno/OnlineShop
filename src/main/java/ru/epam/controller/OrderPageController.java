package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.epam.dto.OrderDto;
import ru.epam.dto.ProductInCartDto;
import ru.epam.models.Order;
import ru.epam.models.OrderInfo;
import ru.epam.repositories.OrderInfoRepository;
import ru.epam.repositories.UserRepository;
import ru.epam.service.order.OrderService;
import ru.epam.service.productincart.ProductInCartService;
import ru.epam.service.user.UserProvider;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER', 'ROLE_SUPERADMIN')")
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderPageController {
    private final ProductInCartService productInCartService;
    private final OrderService orderService;
    private final UserProvider userProvider;
    private final UserRepository userRepository;
    private final OrderInfoRepository orderInfoRepository;

    @GetMapping("")
    public String orderMainPage(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/orders";
    }

    @GetMapping("/order_info{orderId}")
    public String showOrderInfo(@PathVariable("orderId") Long orderId, Model model) {
        List<OrderInfo> ordersInfo = orderInfoRepository.findAllByOrderId(orderId);
        Long totalOrderPrice = ordersInfo.stream().mapToLong(OrderInfo::getTotalPrice).sum();
        model.addAttribute("ordersInfo", ordersInfo);
        model.addAttribute("totalOrderPrice", totalOrderPrice);
        model.addAttribute("orderId", orderId);
        return "order/order_info";
    }

    @GetMapping("/create_order")
    public String createOrder(@ModelAttribute("order") Order order,
                           @ModelAttribute("orderInfo") OrderInfo orderInfo,
                           Model model) {
        boolean isAuthenticated = userProvider.isAuthenticated();
        model.addAttribute("principal", userProvider.getUserName());
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            Long totalPriceAllProducts = productInCartService.getTotalPriceAllProductsInCart();
            model.addAttribute("totalPriceAllProducts", totalPriceAllProducts);
        }
        return "order/create_order";
    }

    @PostMapping("/create_order")
    public String create(@ModelAttribute("order") Order order, Principal principal) throws SQLException {
        Long userId = userRepository.getIdByLogin(principal.getName());
        List<ProductInCartDto> productsInCartByCartId = productInCartService.getProductsInCartByCartId(userId);
        Long totalPrice = productsInCartByCartId.stream().mapToLong(ProductInCartDto::getTotalPrice).sum();
        order.setPrice(totalPrice);
        order.setUserId(userId);
        orderService.saveOrderAndOrderInfo(order);
        return "order/order_accepted";
    }

    @PostMapping("/pay")
    public String payOrder(@RequestParam("orderId") Long orderId) {
        orderService.payOrder(orderId);
        return "redirect:/order";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    @GetMapping("/all_orders")
    public String showAllUserOrdersForAdmin(Model model) {
        List<OrderDto> orderDtos = orderService.getAllOrdersWithUserLogin();
        model.addAttribute("orderDtos", orderDtos);
        return "admin/all_orders";
    }
}
