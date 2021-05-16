package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epam.models.Comment;
import ru.epam.models.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

}
