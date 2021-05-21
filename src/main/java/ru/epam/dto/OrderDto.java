package ru.epam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {
    private Long id;
    private String userLogin;
    private String address;
    private Date date;
    private Long price;
    private boolean hasBennPaid;
}
