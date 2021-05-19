package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.epam.dto.UserDto;
import ru.epam.service.user.UserService;

import java.security.Principal;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPageController {
    private final UserService userService;

    @GetMapping("")
    public String viewUserInfo(Principal principal, Model model) {
        UserDto user = userService.getUserDtoByLogin(principal.getName());
        model.addAttribute("user", user);
        return "user/user";
    }

    @GetMapping("/edit")
    public String editUserInfo(Principal principal, Model model) {
        UserDto user = userService.getUserDtoByLogin(principal.getName());
        model.addAttribute("user", user);
        return "user/user_edit";
    }

    @PostMapping("/edit")
    public String saveUserInfo(UserDto userDto, Principal principal) {
        userService.updateUser(userDto, principal.getName());
        return "redirect:/user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update_role")
    public String updateUserRole(@RequestParam("userId") Long id,
                                 @RequestParam("role") String role) {
        String newRole;
        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/all_users";
        }
        if (role.equals("ROLE_USER")) {
            newRole = "ROLE_BLOCKED";
        } else {
            newRole = "ROLE_USER";
        }
        userService.updateUserRoleById(id, newRole);
        return "redirect:/admin/all_users";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long id,
                                 @RequestParam("role") String role) {
        if (!role.equals("ROLE_ADMIN")) {
            userService.deleteUserById(id);
        }
        return "redirect:/admin/all_users";
    }

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
