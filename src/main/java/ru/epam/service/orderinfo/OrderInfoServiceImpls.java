package ru.epam.service.orderinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.epam.dto.ProductInCartDto;
import ru.epam.models.OrderInfo;
import ru.epam.models.Product;
import ru.epam.models.ProductInCart;
import ru.epam.repositories.OrderInfoRepository;
import ru.epam.repositories.ProductInCartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderInfoServiceImpls implements OrderInfoService {

}
