package ru.epam.models;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private int count;
    private Date publicationDate;
    private int productTypeId;
    private String image;
}
