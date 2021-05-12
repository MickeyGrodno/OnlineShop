package ru.epam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.epam.api.dao.ProductInCartDao;
import ru.epam.models.ProductInCart;


import java.util.List;

@Component
public class ProductInCartDaoImpl implements ProductInCartDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductInCartDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(ProductInCart productInCart) {
        jdbcTemplate.update("INSERT INTO productInCart VALUES (?, ?, ?)",
               productInCart.getProductId(), productInCart.getUserId(), productInCart.getProductCount());
    }

    @Override
    public ProductInCart readUserProduct(int userId) {
        return jdbcTemplate.query("SELECT * FROM productInCart WHERE id=?", new Object[]{userId},
                new BeanPropertyRowMapper<>(ProductInCart.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void updateProductCount(ProductInCart updatedProductInCart) {
        jdbcTemplate.update("UPDATE productInCart SET productCount=? " +
                        "WHERE productId=? AND userId=?", updatedProductInCart.getProductCount(),
                updatedProductInCart.getProductId(), updatedProductInCart. getUserId());
    }

    @Override
    public void delete(int productId, int userId) {
        jdbcTemplate.update("DELETE FROM productInCart WHERE productId=? AND userId=?",
                productId, userId);
    }

    @Override
    public List<ProductInCart> findAll() {
        return jdbcTemplate.query("SELECT * FROM order", new BeanPropertyRowMapper<>(ProductInCart.class));
    }

    @Override
    public List<ProductInCart> findAllByUserId(int userId) {
        return jdbcTemplate.query("SELECT * FROM order WHERE userId=?", new Object[]{userId},
                new BeanPropertyRowMapper<>(ProductInCart.class));
    }
}
