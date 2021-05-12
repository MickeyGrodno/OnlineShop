package ru.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.epam.dao.ProductDaoImpl;
import ru.epam.models.Product;

@Controller
@RequestMapping("/main")
public class MainPageController {
    private final ProductDaoImpl productDaoImpl;

    @Autowired
    public MainPageController(ProductDaoImpl productDaoImpl) {
        this.productDaoImpl = productDaoImpl;
    }

    @GetMapping()
    public String showAllProduct(Model model) {
        Product product = productDaoImpl.read(1);
        model.addAttribute("product", product);
        return "main/allProduct";
    }
}
