package ru.epam.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.epam.config.MyUploadForm;
import ru.epam.models.Product;
import ru.epam.repositories.CommentRepository;
import ru.epam.repositories.ProductInCartRepository;
import ru.epam.repositories.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final ProductInCartRepository productInCartRepository;

    @Override
    public Long saveProduct(Product product) {
        Date date = new Date();
        product.setPublicationDate(date);
        Product savedProduct = productRepository.saveAndFlush(product);
        return savedProduct.getId();
    }

    public String doUpload(MyUploadForm myUploadForm, Long id) {
        String description = myUploadForm.getDescription();
        MultipartFile[] fileDates = myUploadForm.getFilesData();
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
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            String imagePath = product.getImage();
            Path path = Paths.get(imagePath);
            try {
                return Files.readAllBytes(path);
            } catch (NoSuchFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Transactional
    @Override
    public void remove(Long id) {
        commentRepository.deleteAllByProductId(id);
        log.inf
        productInCartRepository.deleteAllByProductId(id);
        productRepository.findById(id).ifPresent(product -> {
            if (product.getImage() != null) {
                File file = new File(product.getImage());
                if (file.delete()) {
                    System.out.println("File has been deleted");
                } else {
                    System.out.println("File not found");
                }
                productRepository.deleteById(id);
            } else {
                productRepository.deleteById(id);
            }
        });
    }
}
