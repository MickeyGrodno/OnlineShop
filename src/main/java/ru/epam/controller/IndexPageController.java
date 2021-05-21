package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.epam.models.Product;
import ru.epam.repositories.ProductRepository;
import ru.epam.service.product.ProductService;
import ru.epam.service.productincart.ProductInCartService;
import ru.epam.service.user.UserProvider;
import ru.epam.service.user.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexPageController {
    private final ProductService productService;
    private final ProductInCartService productInCartService;
    private final UserProvider userProvider;
    private final ProductRepository productRepository;

    @GetMapping()
    public String mainPage(Model model) {
        List<Product> products = productRepository.findAll();
        boolean isAuthenticated = userProvider.isAuthenticated();
        model.addAttribute("products", products);
        model.addAttribute("isAuthenticated", isAuthenticated);
        if(isAuthenticated) {
            Long totalPriceAllProducts = productInCartService.getTotalPriceAllProductsInCart();
            model.addAttribute("totalPriceAllProducts", totalPriceAllProducts);
        }
        return "index";
    }

    @GetMapping("/category_{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        List<Product> products = productRepository.findAllByProductTypeId(id);
        model.addAttribute("products", products);
        return "index";
    }
}
