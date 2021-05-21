package ru.epam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epam.models.Comment;
import ru.epam.models.OrderInfo;

import java.util.List;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
    List<OrderInfo> findAllByOrderId(Long orderId);
}
