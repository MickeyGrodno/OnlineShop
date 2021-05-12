package ru.epam.api.dao;

import ru.epam.models.ProductInCart;

import java.util.List;

public interface ProductInCartDao {
    void save(ProductInCart productInCart);
    ProductInCart readUserProduct(int id);
    void updateProductCount(ProductInCart updatedProductInCart);
    void delete(int productId, int userId);
    List<ProductInCart> findAll();
    List<ProductInCart> findAllByUserId(int userId);
}
