package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.epam.config.MyUploadForm;
import ru.epam.models.Product;
import ru.epam.service.product.ProductService;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {
    private final ProductService productService;

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

    @RequestMapping(value = "/add_image/*", method = RequestMethod.GET)
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
}
