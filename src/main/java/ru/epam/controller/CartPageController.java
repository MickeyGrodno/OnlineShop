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

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartPageController {
    private final ProductInCartService productInCartService;

    @RequestMapping(value = "/cart_{id}")
    public String showProductInfo(@PathVariable Long id, Model model) {
        List<ProductInCartDto> productsInCartByCartId = productInCartService.getProductsInCartByCartId(id);
        model.addAttribute("productsInCartByCartId", productsInCartByCartId);
        return "cart/cart";
    }

    @RequestMapping(value = "/cart_userId")
    public String showProductInfo() {
        return "redirect:../";
    }
}
