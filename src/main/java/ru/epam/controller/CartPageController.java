package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.epam.dto.ProductInCartDto;
import ru.epam.models.Comment;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.models.ProductType;
import ru.epam.service.productincart.ProductInCartService;
import ru.epam.service.productincart.ProductInCartServiceImpl;
import ru.epam.service.user.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartPageController {
    private final ProductInCartService productInCartService;
    private final UserService userService;

    @RequestMapping(value = "/cart_{id}")
    public String showProductInfo(@PathVariable Long id, Principal principal, Model model) {
        List<ProductInCartDto> productsInCartByCartId = productInCartService.getProductsInCartByCartId(id);
        Long totalPrice = productsInCartByCartId.stream().mapToLong(o -> o.getTotalPrice()).sum();
        model.addAttribute("productsInCartByCartId", productsInCartByCartId);
        model.addAttribute("totalPrice", totalPrice);
        if(principal!=null) {
            Long userId = userService.getUserIdByLogin(principal.getName());
            model.addAttribute("principal", principal.getName());
            model.addAttribute("userId", userId);
        } else  {
            model.addAttribute("principal", "noData");
            model.addAttribute("userId", "userId");
        }
        return "cart/cart";
    }

    @RequestMapping(value = "/cart_userId")
    public String showProductInfo() {
        return "redirect:../";
    }
}
