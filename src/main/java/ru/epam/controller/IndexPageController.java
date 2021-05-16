package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.epam.models.Product;
import ru.epam.models.ProductType;
import ru.epam.models.User;
import ru.epam.service.product.ProductService;
import ru.epam.service.producttype.ProductTypeService;
import ru.epam.service.user.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexPageController {
    private final UserService userService;
    private final ProductService productService;
    private final ProductTypeService productTypeService;

    @GetMapping()
    public String mainPage(Principal principal, Model model) {
        List<Product> products = productService.getAllProducts();

        model.addAttribute("products", products);

        if(principal!=null) {
            Long userId = userService.getUserIdByLogin(principal.getName());
            model.addAttribute("principal", principal.getName());
            model.addAttribute("userId", userId);
        } else  {
            model.addAttribute("principal", "noData");
            model.addAttribute("userId", "userId");
        }
        return "index";
    }

    @GetMapping("/category_{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        List<Product> products = productService.getAllProductsByProductTypeId(id);
        //получим одного человека по id из DAO и передать на отображение в представление
        model.addAttribute("products", products);
        return "index";
    }

}
