package ru.epam.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_in_cart")
public class ProductInCart {
    @Id
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "product_count")
    private int productCount;
}
