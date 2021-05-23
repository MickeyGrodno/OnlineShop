package ru.epam.controller.servicecontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginPageController {

    @GetMapping("/login_error")
    public String addProductInCartFast(Model model) {
        model.addAttribute("error", "Неверный логин/пароль.");
        return "login";
    }
}
