package ru.epam.service.producttype;

import ru.epam.models.ProductType;

import java.util.List;

public interface ProductTypeService {
    ProductType getById(Long id);
    List<ProductType> getAllProductTypes();
}
