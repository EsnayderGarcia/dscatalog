package com.snayder.dscatalog.services;

import com.snayder.dscatalog.repositories.ProductRepository;
import com.snayder.dscatalog.services.exceptions.DataBaseException;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private long existingId;

    private long nonExistingId;

    private long dependentId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 145L;
        dependentId = 3L;

        Mockito.doNothing()
                .when(productRepository)
                .deleteById(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class)
                .when(productRepository)
                .deleteById(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class)
                .when(productRepository)
                .deleteById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
        this.productService.delete(existingId);
        });

        Mockito.verify(productRepository).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowAResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.productService.delete(nonExistingId);
        });

        Mockito.verify(productRepository).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowADataBaseExceptionWhenDependentId() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            this.productService.delete(dependentId);
        });

        Mockito.verify(productRepository).deleteById(dependentId);
    }
}