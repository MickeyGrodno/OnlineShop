package ru.epam.service.productincart;

import ru.epam.dto.ProductInCartDto;
import ru.epam.models.ProductInCart;

import java.util.List;

public interface ProductInCartService {
    List<ProductInCart> getAllProdInCartByUserId(Long userId);
    List<ProductInCartDto> getProductsInCartByCartId(Long userId);
    void deleteCartProductsByUserId(Long id);
    void saveProductInCart(ProductInCart productInCart);
    void deleteProductInCartById(Long id);
}
