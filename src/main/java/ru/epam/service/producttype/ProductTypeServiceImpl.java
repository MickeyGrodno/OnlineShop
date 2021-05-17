package ru.epam.service.producttype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.epam.models.ProductType;
import ru.epam.repositories.ProductTypeRepository;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {
    private final ProductTypeRepository productTypeRepository;

    @Override
    public ProductType getById(Long id) {
        return productTypeRepository.findById(id).orElse(null);
    }
}
