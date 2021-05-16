package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.epam.models.Product;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderPageController {

    @GetMapping("/create_order")
    public String mainPage(Principal principal, Model model) {
//        List<Product> products = productService.getAllProducts();
//
//        model.addAttribute("products", products);
//
//        if(principal!=null) {
//            Long userId = userService.getUserIdByLogin(principal.getName());
//            model.addAttribute("principal", principal.getName());
//            model.addAttribute("userId", userId);
//        } else  {
//            model.addAttribute("principal", "noData");
//            model.addAttribute("userId", "userId");
//        }
        return "order/create_order";
    }
}
