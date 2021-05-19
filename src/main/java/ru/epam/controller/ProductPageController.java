package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.epam.models.Comment;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.models.ProductType;
import ru.epam.service.comment.CommentService;
import ru.epam.service.product.ProductService;
import ru.epam.service.producttype.ProductTypeService;
import ru.epam.service.user.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final CommentService commentService;
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/remove/product_{id}", method = RequestMethod.POST)
    public String remove(@PathVariable("id") Long id) {
        productService.remove(id);
        return "redirect:../../admin/product_list";
    }

    @RequestMapping(value = "/{id}")
    public String showProductInfo(@PathVariable Long id,
                                  @ModelAttribute("productInCart") ProductInCart productInCart,
                                  Principal principal,
                                  Model model) {
        Product product = productService.findById(id);
        ProductType productType = productTypeService.getById(product.getProductTypeId());
        List<Comment> commentsByProductId = commentService.getCommentsByProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("productType", productType);
        model.addAttribute("commentsByProductId", commentsByProductId);
        return "product/product_info";
    }

}
