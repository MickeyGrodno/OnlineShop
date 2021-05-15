package ru.epam.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.epam.config.MyUploadForm;
import ru.epam.dto.UploadResultDto;
import ru.epam.models.Product;
import ru.epam.repositories.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Product savedProduct = productRepository.saveAndFlush(product);
        return savedProduct.getId();
    }
    public UploadResultDto doUpload(MyUploadForm myUploadForm, Long id) {

        UploadResultDto uploadResultDto = new UploadResultDto();
        String description = myUploadForm.getDescription();
        System.out.println("Название файла: " + description);

        // Root Directory.
        String uploadRootPath = "src/main/resources/static/images/products";

        System.out.println("uploadRootPath=" + uploadRootPath);

        File uploadRootDir = new File(uploadRootPath);
        // Create directory if it not exists.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        MultipartFile[] fileDatas = myUploadForm.getFileDatas();
        //
        List<File> uploadedFiles = new ArrayList<>();

        for (MultipartFile fileData : fileDatas) {

            // Client File Name
            String name = fileData.getOriginalFilename();
            System.out.println("Client File Name = " + name);

            if (name != null && name.length() > 0) {
                try {
                    String imageFormat = name.split("\\.")[1];
                    String fullFilePath = uploadRootDir.getAbsolutePath() + File.separator + description + "." +imageFormat;

                    Product productById = productRepository.findById(id).get();
                    productById.setImage("images/products/" + description + "." +imageFormat);
                    productRepository.saveAndFlush(productById);
                    // Create the file at server
                    File serverFile = new File(fullFilePath);
                    File newDirectory = new File("\\import\\");
                    newDirectory.mkdirs();
                    Files.write(Paths.get("\\import\\"+description+"."+imageFormat), fileData.getBytes());
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();
                    //
                    uploadedFiles.add(serverFile);
                    System.out.println("Write file: " + serverFile);
                } catch (Exception e) {
                    System.out.println("Error Write file: " + name);
                }
            }
        }

        uploadResultDto.setUploadedFiles(uploadedFiles);
        uploadResultDto.setDescription(description);
        return uploadResultDto;
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
        File file = new File(productRepository.findById(id).get().getImage());
        if(file.delete()){
            System.out.println("File has been deleted");
        }else System.out.println("File not found");
        productRepository.deleteById(id);
    }


}
