package ru.epam.dto;

import lombok.Data;

@Data
public class ProductInCartDto {
    private Long id;
    private String name;
    private Long count;
    private Long price;
    private Long totalPrice;
}
