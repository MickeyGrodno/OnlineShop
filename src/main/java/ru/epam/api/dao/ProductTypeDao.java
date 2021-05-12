package ru.epam.api.dao;

import ru.epam.models.ProductType;

import java.util.List;

public interface ProductTypeDao {
    void save(ProductType productType);
    ProductType read(int id);
    void update(ProductType updatedProductType, int id);
    void delete(int id);
    List<ProductType> findAll();
}
