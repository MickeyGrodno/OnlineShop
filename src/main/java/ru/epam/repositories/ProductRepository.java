package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epam.models.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
//    @Query(nativeQuery = true, value="SELECT * FROM product WHERE id=:myId")
//    Product findByPublicationDateAndName(@Param(value = "myId") Long id);

    List<Product> findAllByProductTypeId(Long id);
}
