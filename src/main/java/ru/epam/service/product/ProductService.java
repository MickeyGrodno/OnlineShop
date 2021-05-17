package ru.epam.service.product;

import ru.epam.config.MyUploadForm;
import ru.epam.models.Product;

import java.util.List;

public interface ProductService {
    Product findById(Long id);
    List<Product> getAllProducts();
    List<Product> getAllProductsByProductTypeId(Long id);
    Long saveProduct(Product product);
    String doUpload(MyUploadForm myUploadForm, Long id);
    byte[] getImageFromProduct(Long id);
    void remove(Long id);
}
