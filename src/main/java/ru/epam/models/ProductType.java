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
@Table(name = "product_type")
public class ProductType {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "category")
    private String category;
    @Column(name = "type")
    private String type;
}
