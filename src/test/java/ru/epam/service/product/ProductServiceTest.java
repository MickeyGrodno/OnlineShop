package ru.epam.service.product;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.epam.OnlineShopTestRunner;
import ru.epam.models.Product;
import ru.epam.repositories.CommentRepository;
import ru.epam.repositories.ProductInCartRepository;
import ru.epam.repositories.ProductRepository;

import java.io.PrintStream;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductServiceTest implements OnlineShopTestRunner {
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ProductInCartRepository productInCartRepository;
    @Autowired
    private ProductService productService;


    @Test
    public void saveProduct() {
        Product product = new Product();
        product.setId(1L);
        when(productRepository.saveAndFlush(product)).thenReturn(product);
        Assert.assertEquals(Optional.of(1L), Optional.of(productService.saveProduct(product)));
    }

    @Test
    public void getImageFromProduct_ProductIsPresent() {
        Long id = 1L;
        Product productMock = Mockito.mock(Product.class);
        when(productMock.getImage()).thenReturn("image");
        when(productRepository.findById(id)).thenReturn(Optional.of(productMock));
        Assert.assertEquals(null, productService.getImageFromProduct(id));
    }

    @Test
    public void remove_RemoveProductFileNotFound() {
        Long id = 1L;
        Product product = new Product();
        product.setImage("image");
        PrintStream stream = mock(PrintStream.class);
        System.setOut(stream);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        productService.remove(id);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(stream, times(1)).println(captor.capture());
        String line = captor.getValue();

        Assert.assertEquals("File not found", line);
    }

    @Test
    public void remove_RemoveProductFileHasNoImage() {
        Long id = 1L;
        Product product = new Product();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        productService.remove(id);
        verify(productRepository, times(1)).deleteById(1L);
    }
}
