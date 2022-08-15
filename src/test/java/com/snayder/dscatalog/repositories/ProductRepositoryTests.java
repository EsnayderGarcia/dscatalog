package com.snayder.dscatalog.repositories;

import com.snayder.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        long existingId = 1L;
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldThrowAnEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
        long nonExistingId = 145L;
        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> repository.deleteById(nonExistingId));
    }
}
