package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.epam.dto.ProductInCartDto;
import ru.epam.models.ProductInCart;
import ru.epam.repositories.ProductInCartRepository;
import ru.epam.repositories.UserRepository;
import ru.epam.service.productincart.ProductInCartService;
import ru.epam.service.user.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ProductInCartPageController {
    private final ProductInCartService productInCartService;
    private final UserRepository userRepository;
    private final ProductInCartRepository productInCartRepository;

    @GetMapping(value = "")
    public String showProductInCartInfo(Principal principal, Model model) {
        Long userId = userRepository.getIdByLogin(principal.getName());
        List<ProductInCartDto> productsInCartByCartId = productInCartService.getProductsInCartByCartId(userId);
        Long totalPrice = productInCartService.getTotalPriceAllProductsInCart();
        model.addAttribute("productsInCartByCartId", productsInCartByCartId);
        model.addAttribute("totalPrice", totalPrice);
        return "cart/cart";
    }

    @PostMapping("/{productId}")
    public String addProductInCart(@PathVariable Long productId,
                                   ProductInCart productInCartFromPage) {

        ProductInCart productInCart = new ProductInCart();
        productInCart.setProductId(productId);
        if (productInCartFromPage.getProductCount() == null || productInCartFromPage.getProductCount() < 1) {
            productInCart.setProductCount(1L);
        } else {
            productInCart.setProductCount(productInCartFromPage.getProductCount());
        }
        productInCartService.saveProductInCart(productInCart);
        return "redirect:../";
    }

    @GetMapping("/{productId}")
    public String addProductInCartFast(@PathVariable Long productId,
                                       Principal principal) {
        Long userId = userRepository.getIdByLogin(principal.getName());
        ProductInCart productInCart = new ProductInCart();
        productInCart.setUserId(userId);
        productInCart.setProductId(productId);
        productInCart.setProductCount(1L);
        productInCartService.saveProductInCart(productInCart);
        return "redirect:../";
    }

    @PostMapping("/delete/{productId}")
    public String deleteProductFromCart(Principal principal,
                                        @PathVariable Long productId) {
        Long userId = userRepository.getIdByLogin(principal.getName());
        productInCartRepository.deleteByUserIdAndProductId(userId, productId);
        return "redirect:/cart";
    }
}
