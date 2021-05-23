package ru.epam.service.productincart;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.epam.OnlineShopTestRunner;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.repositories.ProductInCartRepository;
import ru.epam.repositories.ProductRepository;
import ru.epam.repositories.UserRepository;
import ru.epam.service.user.UserProvider;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductInCartServiceTest implements OnlineShopTestRunner {
    @MockBean
    private ProductInCartRepository productInCartRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private UserProvider userProvider;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductInCart productInCartMock;
    @Autowired
    private ProductInCartService productInCartService;

    @Test
    public void saveProductInCart_ProductInCartPresentInDB() {
        ProductInCart productInCart = new ProductInCart();
        productInCart.setProductCount(2L);
        productInCart.setUserId(1L);
        productInCart.setProductId(1L);
        ProductInCart productInCartFrobDBMock = Mockito.mock(ProductInCart.class);
        when(userProvider.getUserName()).thenReturn("name");
        when(userRepository.getIdByLogin("name")).thenReturn(1L);
        when(productInCartRepository.findByUserIdAndProductId(productInCart.getUserId(), productInCart.getUserId())).thenReturn(productInCartFrobDBMock);
        productInCartService.saveProductInCart(productInCart);
        verify(productInCartFrobDBMock, times(1)).setProductCount(anyLong());
    }

    @Test
    public void saveProductInCart_ProductInCartNotPresentInDB() {
        ProductInCart productInCart = new ProductInCart();
        ProductInCart productInCartFromDB = new ProductInCart();
        productInCart.setProductCount(2L);
        productInCart.setUserId(1L);
        productInCart.setProductId(1L);
        productInCartFromDB.setProductCount(4L);
        when(userProvider.getUserName()).thenReturn("name");
        when(userRepository.getIdByLogin("name")).thenReturn(1L);
        when(productInCartRepository.findByUserIdAndProductId(productInCart.getUserId(), productInCart.getUserId())).thenReturn(null);
        productInCartService.saveProductInCart(productInCart);
        verify(productInCartRepository, times(1)).saveAndFlush(productInCart);
    }

    @Test
    public void getProductsInCartByCartId_ProductsFound() {
        ProductInCart productInCart1 = new ProductInCart();
        ProductInCart productInCart2 = new ProductInCart();
        productInCart1.setProductId(1L);
        productInCart1.setUserId(1L);
        productInCart1.setProductCount(1L);
        productInCart2.setProductId(2L);
        productInCart2.setUserId(1L);
        productInCart2.setProductCount(1L);
        List<ProductInCart> allUserProducts = new ArrayList<>();
        allUserProducts.add(productInCart1);
        allUserProducts.add(productInCart2);
        List<Product> products = new ArrayList<>();
        Product product1 = new Product(1L, "prod", 12L, new Date(), 1L, "image", "text");
        Product product2 = new Product(2L, "prod", 12L, new Date(), 1L, "image", "text");
        products.add(product1);
        products.add(product2);
        Long userId = 1L;
        when(productInCartRepository.getAllByUserId(userId)).thenReturn(allUserProducts);
        when(productRepository.findAllById(anyList())).thenReturn(products);
        Assert.assertEquals(2, productInCartService.getProductsInCartByCartId(userId).size());
    }

    @Test
    public void getProductsInCartByCartId_ProductsNotFound() {
        List<ProductInCart> allUserProducts = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        Long userId = 1L;
        when(productInCartRepository.getAllByUserId(userId)).thenReturn(allUserProducts);
        when(productRepository.findAllById(anyList())).thenReturn(products);
        Assert.assertEquals(0, productInCartService.getProductsInCartByCartId(userId).size());
    }

    @Test
    public void getTotalPriceAllProductsInCart() {

        String login = "login";
        Long userId = 1L;
        ProductInCart productInCart1 = new ProductInCart();
        ProductInCart productInCart2 = new ProductInCart();
        productInCart1.setProductId(1L);
        productInCart1.setUserId(1L);
        productInCart1.setProductCount(1L);
        productInCart2.setProductId(2L);
        productInCart2.setUserId(1L);
        productInCart2.setProductCount(1L);
        List<ProductInCart> userProductsInCart = new ArrayList<>();
        userProductsInCart.add(productInCart1);
        userProductsInCart.add(productInCart2);
        List<Product> products = new ArrayList<>();
        Product product1 = new Product(1L, "prod", 12L, new Date(), 1L, "image", "text");
        Product product2 = new Product(2L, "prod", 12L, new Date(), 1L, "image", "text");
        products.add(product1);
        products.add(product2);

        when(userProvider.getUserName()).thenReturn(login);
        when(userRepository.getIdByLogin(login)).thenReturn(userId);
        when(productInCartRepository.findAllByUserId(userId)).thenReturn(userProductsInCart);
        when(productRepository.findAllById(anySet())).thenReturn(products);
        Long sum = productInCartService.getTotalPriceAllProductsInCart();
        Assert.assertEquals(Optional.of(24L), Optional.of(productInCartService.getTotalPriceAllProductsInCart()));
    }
}
