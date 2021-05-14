package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epam.models.Comment;
import ru.epam.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
