package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.epam.dto.UserDto;
import ru.epam.models.ProductInCart;
import ru.epam.models.User;
import ru.epam.service.user.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPageController {
private final UserService userService;

    @GetMapping( "")
    public String viewUserInfo(Principal principal, Model model) {
        UserDto user = userService.getUserDtoByLogin(principal.getName());
        model.addAttribute("user", user);
        return "user/user";
    }
    @GetMapping( "/edit")
    public String editUserInfo(Principal principal, Model model) {
        UserDto user = userService.getUserDtoByLogin(principal.getName());
        model.addAttribute("user", user);
        return "user/user_edit";
    }
    @PostMapping( "/edit")
    public String saveUserInfo(UserDto userDto, Principal principal) {
        userService.updateUser(userDto, principal.getName());
        return "redirect:/user";
    }

//    @PostMapping( "/cart_{userId}/{productId}")
//    public String addProductInCartFast(@PathVariable Long userId,
//                                       @PathVariable Long productId,
//                                       @ModelAttribute("productInCart") ProductInCart productInCart) {
//        ProductInCart productInCartToDB = new ProductInCart();
//        productInCartToDB.setUserId(userId);
//        productInCartToDB.setProductId(productId);
//        productInCartToDB.setProductCount(productInCart.getProductCount());
//        productInCartService.saveProductInCart(productInCart);
//        return "redirect:../../";
//    }
}
