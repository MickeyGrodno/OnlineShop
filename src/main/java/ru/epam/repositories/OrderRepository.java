package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.epam.models.Comment;
import ru.epam.models.Order;
import ru.epam.models.ProductInCart;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
//    @Query(nativeQuery = true, value="INSERT INTO orders VALUES ")
//    Long getIdByLogin(@Param(value = "login") String login);

    @Query(nativeQuery = true, value = "select * from orders o where o.user_id=:userId")
    List<Order> findAllByUserId(Long userId);
}
