package ru.epam.api.dao;

import ru.epam.models.Product;

import java.util.List;

public interface ProductDao {
    void save(Product product);
    Product read(int id);
    void update(Product updatedProduct, int id);
    void delete(int id);
    List<Product> findAll();
    List<Product> findAllByProductType(int productTypeId);
}
