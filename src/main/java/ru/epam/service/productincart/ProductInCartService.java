package ru.epam.service.productincart;

import ru.epam.dto.ProductInCartDto;
import ru.epam.models.ProductInCart;

import java.util.List;

public interface ProductInCartService {
    List<ProductInCartDto> getProductsInCartByCartId(Long userId);

    void saveProductInCart(ProductInCart productInCart);

    Long getTotalPriceAllProductsInCart();

}
