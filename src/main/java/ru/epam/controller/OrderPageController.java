package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.epam.dto.ProductInCartDto;
import ru.epam.models.Order;
import ru.epam.models.Product;
import ru.epam.service.order.OrderService;
import ru.epam.service.productincart.ProductInCartService;
import ru.epam.service.user.UserService;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderPageController {
    private final UserService userService;
    private final ProductInCartService productInCartService;
    private final OrderService orderService;

    @RequestMapping(value = "/create_order", method = RequestMethod.GET)
    public String mainPage(@ModelAttribute("order") Order order, Principal principal, Model model) {
        Long userId = userService.getUserIdByLogin(principal.getName());
        model.addAttribute("principal", principal.getName());
        model.addAttribute("userId", userId);
        return "order/create_order";
    }

    @RequestMapping(value = "/create_order", method = RequestMethod.POST)
    public String create(@ModelAttribute("order") Order order, Principal principal) throws SQLException {
        Long userId = userService.getUserIdByLogin(principal.getName());
        List<ProductInCartDto> productsInCartByCartId = productInCartService.getProductsInCartByCartId(userId);
        Long totalPrice = productsInCartByCartId.stream().mapToLong(o -> o.getTotalPrice()).sum();
        order.setPrice(totalPrice);
        order.setUserId(userId);
        orderService.saveOrder(order);
        productInCartService.deleteCartProductsByUserId(userId);
        return "redirect:../";
    }
}
