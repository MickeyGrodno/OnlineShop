package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.epam.config.MyUploadForm;
import ru.epam.models.Product;
import ru.epam.service.product.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
    public String create(@ModelAttribute("product") Product product, Model model) throws SQLException {
        Date date = new Date();
        product.setPublicationDate(date);
        Long productId = productService.saveProduct(product);
        return "redirect:/admin/add_image/" + productId;
    }
    @RequestMapping(value = "/add_image/{id}", method = RequestMethod.GET)
    public String uploadOneFileHandler(@PathVariable("id") Long id, Model model) {
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

    @RequestMapping(value = "/product_delete_{id}", method = RequestMethod.POST)
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.remove(id);
        return "redirect:/admin/product_list";
    }

}
