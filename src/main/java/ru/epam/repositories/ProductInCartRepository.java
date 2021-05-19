package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.epam.models.ProductInCart;

import java.util.List;

public interface ProductInCartRepository extends JpaRepository<ProductInCart, Long> {
    List<ProductInCart> getAllByUserId(Long userId);

    void removeAllByUserId(Long userId);

    @Modifying
    @Query(nativeQuery = true, value = "delete from product_in_cart p where p.user_id=:userId and p.product_id=:productId")
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    ProductInCart findByUserIdAndProductId(Long userId, Long productId);

    @Query(nativeQuery = true, value = "select p.product_count from product_in_cart p where p.user_id=:userId")
    Long getProductCountByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "select p.total_price from product_in_cart p where p.user_id=:userId")
    List<Long> getProductInCartTotalPricesByUserId(@Param("userId")Long userId);

    @Query(nativeQuery = true, value = "select * from product_in_cart p where p.user_id=:userId")
    List<ProductInCart> findAllByUserId(Long userId);
}
