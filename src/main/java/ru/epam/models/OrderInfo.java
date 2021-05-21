package ru.epam.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_info")
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_count")
    private Long productCount;
    @Column(name = "price")
    private Long price;
    @Column(name = "total_price")
    private Long totalPrice;
}
