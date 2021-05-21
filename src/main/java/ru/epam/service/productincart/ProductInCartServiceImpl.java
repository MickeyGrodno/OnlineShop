package ru.epam.service.productincart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.epam.dto.ProductInCartDto;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.repositories.ProductInCartRepository;
import ru.epam.repositories.ProductRepository;
import ru.epam.repositories.UserRepository;
import ru.epam.service.user.UserProvider;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductInCartServiceImpl implements ProductInCartService {
    private final ProductInCartRepository productInCartRepository;
    private final ProductRepository productRepository;
    private final UserProvider userProvider;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void deleteCartProductsByUserId(Long id) {
        productInCartRepository.removeAllByUserId(id);
    }

    @Override
    public void saveProductInCart(ProductInCart productInCart) {
        ProductInCart productInCartFromDB = productInCartRepository.findByUserIdAndProductId(productInCart.getUserId(), productInCart.getProductId());

        if (productInCartFromDB != null) {
            productInCartFromDB.setProductCount(productInCart.getProductCount() + productInCartFromDB.getProductCount());
            productInCartRepository.saveAndFlush(productInCartFromDB);
        } else {
            productInCartRepository.saveAndFlush(productInCart);
        }
    }

    public List<ProductInCartDto> getProductsInCartByCartId(Long userId) {
        List<ProductInCart> allUserProducts = productInCartRepository.getAllByUserId(userId);
        List<Long> productsId = allUserProducts.stream().map(ProductInCart::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productsId);
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
    @Transactional
    public void deleteProductInCartByUserIdAndProductId(Long userId, Long productId) {
        productInCartRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public Long getTotalPriceAllProductsInCart() {
        String login = userProvider.getUsername();
        Long userId = userRepository.getIdByLogin(login);
        List<ProductInCart> userProductsInCart = productInCartRepository.findAllByUserId(userId);
        Set<Long> productIds = userProductsInCart
                .stream()
                .map(ProductInCart::getProductId)
                .collect(Collectors.toSet());

        List<Product> products = productRepository.findAllById(productIds);
        Map<Long, Long> productIdToPrice = products
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        Long totalPrice = userProductsInCart
                .stream()
                .map(a -> a.getProductCount() * productIdToPrice.get(a.getProductId()))
                .mapToLong(Long::longValue)
                .sum();

        return totalPrice;
    }
}
