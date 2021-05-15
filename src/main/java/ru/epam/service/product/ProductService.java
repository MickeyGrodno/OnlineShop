package ru.epam.service.product;

import org.springframework.ui.Model;
import ru.epam.config.MyUploadForm;
import ru.epam.dto.UploadResultDto;
import ru.epam.models.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface ProductService {
    Product findById(Long id);
    List<Product> getAllProducts();
    List<Product> getAllProductsByProductTypeId(Long id);
    Long saveProduct(Product product);
    String doUpload(MyUploadForm myUploadForm, Long id);
    byte[] getImageFromProduct(Long id);
    void remove(Long id);
//    Product findByPublicationDate(Date date);
}
