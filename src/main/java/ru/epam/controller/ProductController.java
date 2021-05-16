package ru.epam.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.epam.models.Comment;
import ru.epam.models.Product;
import ru.epam.models.ProductType;
import ru.epam.models.User;
import ru.epam.service.comment.CommentService;
import ru.epam.service.product.ProductService;
import ru.epam.service.producttype.ProductTypeService;
import ru.epam.service.user.UserService;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final ProductTypeService productTypeService;
  private final CommentService commentService;
  private final UserService userService;

  @RequestMapping(value = "/remove/product_{id}", method = RequestMethod.POST)
  public String remove(@PathVariable("id") Long id) {
    productService.remove(id);
    return "redirect:../../admin/product_list";
  }

  @RequestMapping(value = "/product_{id}")
  public String showProductInfo(@PathVariable Long id, Model model) {
    Product product = productService.findById(id);
    ProductType productType = productTypeService.getById(product.getProductTypeId());
    List<Comment> commentsByProductId = commentService.getCommentsByProductId(id);
    model.addAttribute("product", product);
    model.addAttribute("productType", productType);
    model.addAttribute("commentsByProductId", commentsByProductId);
    return "product/product_info";
  }

}
