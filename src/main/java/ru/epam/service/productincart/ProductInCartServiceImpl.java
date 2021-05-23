package ru.epam.service.productincart;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.epam.dto.ProductInCartDto;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.repositories.ProductInCartRepository;
import ru.epam.repositories.ProductRepository;
import ru.epam.repositories.UserRepository;
import ru.epam.service.user.UserProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Log4j2
@Service
@RequiredArgsConstructor
public class ProductInCartServiceImpl implements ProductInCartService {
    private final ProductInCartRepository productInCartRepository;
    private final ProductRepository productRepository;
    private final UserProvider userProvider;
    private final UserRepository userRepository;

    @Override
    public void saveProductInCart(ProductInCart productInCart) {
        Long userId = userRepository.getIdByLogin(userProvider.getUserName());
        productInCart.setUserId(userId);
        ProductInCart productInCartFromDB = productInCartRepository.findByUserIdAndProductId(productInCart.getUserId(), productInCart.getProductId());

        if (productInCartFromDB != null) {
            productInCartFromDB.setProductCount(productInCart.getProductCount() + productInCartFromDB.getProductCount());
            productInCartRepository.saveAndFlush(productInCartFromDB);
        } else {
            productInCartRepository.saveAndFlush(productInCart);
            log.info("The product is saved in the database.");
        }
    }

    public List<ProductInCartDto> getProductsInCartByCartId(Long userId) {
        List<ProductInCart> allUserProducts = productInCartRepository.getAllByUserId(userId);
        log.info("All items in the user #{} basket have been loaded.", userId);
        List<Long> productsId = allUserProducts.stream().map(ProductInCart::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productsId);
        log.info("All products have been loaded.");
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, product -> product));
        List<ProductInCartDto> resultDtos = new ArrayList<>();

        for (ProductInCart productInCart : allUserProducts) {
            ProductInCartDto dto = new ProductInCartDto();
            dto.setId(productInCart.getProductId());
            dto.setCount(productInCart.getProductCount());
            Product product = productMap.get(productInCart.getProductId());
            dto.setPrice(product.getPrice());
            dto.setName(product.getName());
            dto.setTotalPrice(dto.getCount() * dto.getPrice());
            resultDtos.add(dto);
        }
        return resultDtos;

    }

    @Override
    public Long getTotalPriceAllProductsInCart() {
        String login = userProvider.getUserName();
        Long userId = userRepository.getIdByLogin(login);
        log.info("All items in the user #{} cart have been loaded.", userId);
        List<ProductInCart> userProductsInCart = productInCartRepository.findAllByUserId(userId);
        log.info("All items in cart by user #{} have been loaded.", userId);
        Set<Long> productIds = userProductsInCart
                .stream()
                .map(ProductInCart::getProductId)
                .collect(Collectors.toSet());

        List<Product> products = productRepository.findAllById(productIds);
        log.info("All products has been loaded by ID.");
        Map<Long, Long> productIdToPrice = products
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        return userProductsInCart
                .stream()
                .map(a -> a.getProductCount() * productIdToPrice.get(a.getProductId()))
                .mapToLong(Long::longValue)
                .sum();
    }
}
