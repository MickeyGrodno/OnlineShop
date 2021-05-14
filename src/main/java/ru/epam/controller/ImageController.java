package ru.epam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.epam.service.product.ProductService;

@RequiredArgsConstructor
@RestController
public class ImageController {
private final ProductService productService;

    @GetMapping("image/{id}")
    public byte[] getImage(@PathVariable Long id) {
        return productService.getImageFromProduct(id);
    }
}
