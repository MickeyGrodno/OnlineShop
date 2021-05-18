package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.epam.dto.ProductInCartDto;
import ru.epam.models.ProductInCart;
import ru.epam.service.productincart.ProductInCartService;
import ru.epam.service.user.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ProductInCartPageController {
    private final ProductInCartService productInCartService;
    private final UserService userService;

    @GetMapping(value = "")
    public String showProductInfo(Principal principal, Model model) {
        Long userId = userService.getUserIdByLogin(principal.getName());
        List<ProductInCartDto> productsInCartByCartId = productInCartService.getProductsInCartByCartId(userId);
        Long totalPrice = productsInCartByCartId.stream().mapToLong(ProductInCartDto::getTotalPrice).sum();
        model.addAttribute("productsInCartByCartId", productsInCartByCartId);
        model.addAttribute("totalPrice", totalPrice);
        return "cart/cart";
    }

    @PostMapping("/{productId}")
    public String addProductInCart(@PathVariable Long productId,
                                       Principal principal,
                                       ProductInCart productInCartFromPage) {
        Long userId = userService.getUserIdByLogin(principal.getName());
        ProductInCart productInCart = new ProductInCart();
        productInCart.setUserId(userId);
        productInCart.setProductId(productId);
        if(Objects.isNull(productInCartFromPage) || productInCartFromPage.getProductCount()==0) {
            productInCart.setProductCount(1);
        } else {
            productInCart.setProductCount(productInCartFromPage.getProductCount());
        }
        productInCartService.saveProductInCart(productInCart);
        return "redirect:../";
    }

    @GetMapping("/{productId}")
    public String addProductInCartFast(@PathVariable Long productId,
                                   Principal principal) {
        Long userId = userService.getUserIdByLogin(principal.getName());
        ProductInCart productInCart = new ProductInCart();
        productInCart.setUserId(userId);
        productInCart.setProductId(productId);
        productInCart.setProductCount(1);
        productInCartService.saveProductInCart(productInCart);
        return "redirect:../";
    }

    @PostMapping("/delete/{productId}")
    public String deleteProductFromCart(Principal principal,
                                        @PathVariable Long productId) {
        Long userId = userService.getUserIdByLogin(principal.getName());
        productInCartService.deleteProductInCartByUserIdAndProductId(userId, productId);
        return "redirect:/cart";
    }
}
