package ru.epam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.epam.api.dao.ProductTypeDao;
import ru.epam.models.ProductType;

import java.util.List;

@Component
public class ProductTypeDaoImpl implements ProductTypeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductTypeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void save(ProductType productType) {
        jdbcTemplate.update("INSERT INTO productType (category, classification) " +
                        "VALUES (?, ?)", productType.getCategory(), productType.getClassification());
    }

    @Override
    public ProductType read(int id) {
        return jdbcTemplate.query("SELECT * FROM user WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(ProductType.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void update(ProductType updatedProductType, int id) {
        jdbcTemplate.update("UPDATE productType SET category=?, classification=? WHERE id=?",
                updatedProductType.getCategory(), updatedProductType.getClassification(), id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM productType WHERE id=?", id);
    }

    @Override
    public List<ProductType> findAll() {
        return jdbcTemplate.query("SELECT * FROM productType",
                new BeanPropertyRowMapper<>(ProductType.class));
    }
}
