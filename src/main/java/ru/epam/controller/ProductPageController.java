package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.epam.dto.CommentDto;
import ru.epam.models.Comment;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.models.ProductType;
import ru.epam.repositories.ProductRepository;
import ru.epam.repositories.ProductTypeRepository;
import ru.epam.repositories.UserRepository;
import ru.epam.service.comment.CommentService;
import ru.epam.service.product.ProductService;
import ru.epam.service.productincart.ProductInCartService;
import ru.epam.service.user.UserProvider;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductService productService;
    private final CommentService commentService;
    private final UserProvider userProvider;
    private final UserRepository userRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductRepository productRepository;
    private final ProductInCartService productInCartService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    @PostMapping("/remove/product/{id}")
    public String remove(@PathVariable("id") Long id) {
        productService.remove(id);
        return "redirect:../../../admin/product_list";
    }

    @RequestMapping(value = "/{productId}")
    public String showProductInfo(@PathVariable Long productId,
                                  @ModelAttribute("productInCart") ProductInCart productInCart,
                                  Principal principal,
                                  Model model) {
        boolean isAuthenticated = userProvider.isAuthenticated();
        Product product = productRepository.findById(productId).orElse(null);
        ProductType productType = productTypeRepository.findById(product.getProductTypeId()).orElse(null);
        List<CommentDto> commentsDto = commentService.getCommentsDtoByProductId(productId);
        if (isAuthenticated) {
            Long totalPriceAllProducts = productInCartService.getTotalPriceAllProductsInCart();
            Long userId = userRepository.getIdByLogin(principal.getName());
            Comment comment = new Comment();
            model.addAttribute("totalPriceAllProducts", totalPriceAllProducts);
            model.addAttribute("userId", userId);
            model.addAttribute("comment", comment);
        }
        model.addAttribute("product", product);
        model.addAttribute("productType", productType);
        model.addAttribute("commentsDto", commentsDto);
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "product/product_info";
    }

}
