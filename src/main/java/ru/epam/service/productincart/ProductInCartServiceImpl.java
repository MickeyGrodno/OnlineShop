package ru.epam.service.productincart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.epam.dto.ProductInCartDto;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.repositories.ProductInCartRepository;
import ru.epam.repositories.ProductRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductInCartServiceImpl implements ProductInCartService{
    private final ProductInCartRepository productInCartRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ProductInCart> getAllProdInCartByUserId(Long userId) {
        return productInCartRepository.getAllByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteCartProductsByUserId(Long id) {
        productInCartRepository.removeAllByUserId(id);
    }

    @Override
    public void saveProductInCart(ProductInCart productInCart) {
        ProductInCart productInCartFromDB =
                getProductInCartByUserIdAndProduct(productInCart.getUserId(), productInCart.getProductId());
        if(productInCartFromDB!=null) {
            productInCart.setProductCount(productInCart.getProductCount()+productInCartFromDB.getProductCount());
            productInCartRepository.saveAndFlush(productInCart);
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
            dto.setTotalPrice(dto.getCount()*dto.getPrice());
            resultDtos.add(dto);
        }
        return resultDtos;

    }

    @Override
    public void deleteProductInCartById(Long id) {
        productInCartRepository.deleteById(id);
    }

    private ProductInCart getProductInCartByUserIdAndProduct(Long userId, Long productId) {
        return productInCartRepository.findProductInCartByUserIdAndProductId(userId, productId);
    }
}
