package ru.epam.controller.servicecontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccessDeniedPageController {

    @GetMapping("/access_denied")
    public String accessDenied() {
        return "access_denied";
    }
}