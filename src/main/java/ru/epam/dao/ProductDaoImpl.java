package ru.epam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.epam.api.dao.ProductDao;
import ru.epam.models.Product;

import java.util.List;

@Component
public class ProductDaoImpl implements ProductDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update("INSERT INTO product (name, price, count, publicationDate, productTypeId, image)" +
                        "VALUES (?, ?, ?, ?, ?, ?)", product.getName(), product.getPrice(), product.getCount(),
                product.getPublicationDate(), product.getProductTypeId(), product.getImage());
    }

    @Override
    public Product read(int id) {
        return jdbcTemplate.query("SELECT * FROM product WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Product.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void update(Product updatedProduct, int id) {
        jdbcTemplate.update("UPDATE product SET name=?, price=?, count=?, productTypeId=?, image=?" +
                        "WHERE id=?", updatedProduct.getName(), updatedProduct.getPrice(), updatedProduct.getCount(),
                updatedProduct.getProductTypeId(), updatedProduct.getImage());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM product WHERE id=?", id);
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM product", new BeanPropertyRowMapper<>(Product.class));
    }

    @Override
    public List<Product> findAllByProductType(int productTypeId) {
        return jdbcTemplate.query("SELECT * FROM product WHERE=?", new Object[]{productTypeId},
                new BeanPropertyRowMapper<>(Product.class));
    }
}
