package ru.epam.controller;

import lombok.RequiredArgsConstructor;
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

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexPageController {
    private final UserService userService;
    private final ProductService productService;
    private final ProductTypeService productTypeService;

    @GetMapping()
    public String mainPage(Model model) {
//        Product product = productDaoImpl.read(1);
        List<Product> products = productService.getAllProducts();
//        List<ProductType> productTypes = productTypeService.getAllProductTypes();
        model.addAttribute("products", products);
//        model.addAttribute("productTypes", productTypes);
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
