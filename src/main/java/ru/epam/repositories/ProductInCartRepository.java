package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epam.models.Comment;
import ru.epam.models.ProductInCart;

import java.util.List;

public interface ProductInCartRepository extends JpaRepository<ProductInCart, Long> {
    List<ProductInCart> getAllByUserId(Long userId);
    void removeAllByUserId(Long userId);
}
