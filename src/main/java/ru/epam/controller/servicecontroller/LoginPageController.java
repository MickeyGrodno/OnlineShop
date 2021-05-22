package ru.epam.controller.servicecontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.epam.models.ProductInCart;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class LoginPageController {

    @GetMapping("/login_error")
    public String addProductInCartFast(Model model) {
        model.addAttribute("error", "Неверный логин/пароль.");
        return "login";
    }
}
