package ru.epam.service.product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.epam.config.MyUploadForm;
import ru.epam.dto.UploadResultDto;
import ru.epam.models.Product;
import ru.epam.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    public Product findById(Long id) {
        return productRepository.findById(id).get();
//        return productRepository.findById(id).get();
    }

    public List<Product> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts;
    }

    @Override
    public List<Product> getAllProductsByProductTypeId(Long id) {
        return productRepository.findAllByProductTypeId(id);
    }

    @Override
    public Long saveProduct(Product product) {
        Date date = new Date();
        product.setPublicationDate(date);
        Product savedProduct = productRepository.saveAndFlush(product);
        return savedProduct.getId();
    }

    public String doUpload(MyUploadForm myUploadForm, Long id) {
        String description = myUploadForm.getDescription();
        MultipartFile[] fileDates = myUploadForm.getFileDatas();
        for (MultipartFile fileData : fileDates) {
            String name = fileData.getOriginalFilename();
            String imageFormat = name.split("\\.")[1];
            String fileName = "\\EPAM\\OnlineShop\\products\\" + description + "." + imageFormat;
            File newDirectory = new File("\\EPAM\\OnlineShop\\products\\");
            newDirectory.mkdirs();
            try {
                Files.write(Paths.get(fileName), fileData.getBytes());
                updateImagePath(id, fileName);
            } catch (Exception ignored) {
            }
        }
        return description;
    }

    private void updateImagePath(Long id, String fileName) {
        productRepository.findById(id).ifPresent(product -> {
            product.setImage(fileName);
            productRepository.saveAndFlush(product);
        });
    }

    @Override
    public byte[] getImageFromProduct(Long id) {
        Product product = productRepository.findById(id).get();
        String imagePath = product.getImage();
        Path path = Paths.get(imagePath);
        try {
            byte[] content = Files.readAllBytes(path);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(Long id) {
        if (productRepository.findById(id).get().getImage() != null) {
            File file = new File(productRepository.findById(id).get().getImage());
            if (file.delete()) {
                System.out.println("File has been deleted");
            } else System.out.println("File not found");
            productRepository.deleteById(id);
        }
        else {
            productRepository.deleteById(id);
        }
    }
}
