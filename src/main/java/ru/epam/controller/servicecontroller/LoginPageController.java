package ru.epam.controller.servicecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
public class LoginPageController {

    @GetMapping("/login_error")
    public String loginError(Model model) {
        model.addAttribute("error", "Неверный логин/пароль.");
        return "login";
    }


}
