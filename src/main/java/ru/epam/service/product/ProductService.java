package ru.epam.service.product;

import ru.epam.config.MyUploadForm;
import ru.epam.models.Product;

public interface ProductService {
    Long saveProduct(Product product);

    String doUpload(MyUploadForm myUploadForm, Long id);

    byte[] getImageFromProduct(Long id);

    void remove(Long id);
}
