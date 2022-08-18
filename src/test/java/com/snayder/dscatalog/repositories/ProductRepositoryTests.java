package com.snayder.dscatalog.repositories;

import com.snayder.dscatalog.entities.Product;
import com.snayder.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repository;

    private long existingId;

    private long nonExistingId;

    private Long totalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 145L;
        totalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        this.repository.deleteById(existingId);
        Optional<Product> result = this.repository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldThrowAnEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> this.repository.deleteById(nonExistingId));
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Product product = Factory.createProduct();

        product.setId(null);
        product = this.repository.save(product);

            Assertions.assertNotNull(product.getId());
            Assertions.assertEquals(totalProducts + 1, product.getId());
            Assertions.assertEquals(Product.class, product.getClass());
    }

}
