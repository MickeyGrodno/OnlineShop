package ru.epam.controller;

import javassist.NotFoundException;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/change_password")
    public String changePassword(@RequestParam("userId") Long userId,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) throws NotFoundException {
        System.out.println();
        if(userService.updateUserPassword(userId, oldPassword, newPassword)){
            return "redirect:/user";
        } else {
//            model.addAttribute("user", user);
            return "redirect:/user/edit";
        }
    }

    @PostMapping("/edit")
    public String saveUserInfo(UserDto userDto, Principal principal) {
        userService.updateUser(userDto, principal.getName());
        return "redirect:/user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update_role")
    public String updateUserRole(@RequestParam("userId") Long id,
                                 @RequestParam("role") String role) throws NotFoundException {
        userService.updateUserRoleById(id, role);
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
