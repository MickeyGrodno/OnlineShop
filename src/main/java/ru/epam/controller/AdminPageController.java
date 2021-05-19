package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.epam.config.MyUploadForm;
import ru.epam.models.Product;
import ru.epam.models.User;
import ru.epam.service.product.ProductService;
import ru.epam.service.user.UserService;

import javax.annotation.security.RolesAllowed;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {
    private final ProductService productService;
    private final UserService userService;

    @GetMapping()
    public String adminMain(@ModelAttribute("product") Product product) {
        return "admin/main";
    }

    @GetMapping("/new_product")
    public String newProduct(@ModelAttribute("product") Product product) {
        return "admin/new_product";
    }

    @PostMapping("/new_product")
    public String create(@ModelAttribute("product") Product product) throws SQLException {
        Long productId = productService.saveProduct(product);
        return "redirect:/admin/add_image/" + productId;
    }

    @GetMapping( "/add_image/*")
    public String uploadOneFileHandler(Model model) {
        MyUploadForm myUploadForm = new MyUploadForm();
        model.addAttribute("myUploadForm", myUploadForm);
        return "admin/add_product_image";
    }

    @RequestMapping(value = "/add_image/{id}", method = RequestMethod.POST)
    public String uploadImage(@PathVariable("id") Long id, Model model,
                              @ModelAttribute("myUploadForm") MyUploadForm myUploadForm) {
        String description = productService.doUpload(myUploadForm, id);
        model.addAttribute("description", description);
        return "admin/main";
    }

    @GetMapping("/product_list")
    public String editOrDeleteProductList(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/product_list";
    }

    @GetMapping("/all_users")
    public String getAllUsersList(Model model) {
        List<User> users = userService.getAllUsers();
        List<User> sortedUsers = users.stream()
                .sorted(Comparator.comparingDouble(User::getId))
                .collect(Collectors.toList());
        model.addAttribute("users", sortedUsers);
        return "admin/all_users";
    }


}
