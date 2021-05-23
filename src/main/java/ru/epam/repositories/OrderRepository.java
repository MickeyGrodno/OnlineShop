package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.epam.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(nativeQuery = true, value = "select * from orders o where o.user_id=:userId")
    List<Order> findAllByUserId(Long userId);

    void deleteAllByUserId(Long id);

    Optional<Order> findById(Long id);
}
