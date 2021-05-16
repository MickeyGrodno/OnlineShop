package ru.epam.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private Long price;
    @Column(name = "publication_date")
    private Date publicationDate;
    @Column(name = "product_type_id")
    private Long productTypeId;
    @Column(name = "image")
    private String image;
    @Column(name = "text")
    private String text;
}