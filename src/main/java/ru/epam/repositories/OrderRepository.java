package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.epam.models.Comment;
import ru.epam.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
//    @Query(nativeQuery = true, value="INSERT INTO orders VALUES ")
//    Long getIdByLogin(@Param(value = "login") String login);
}
