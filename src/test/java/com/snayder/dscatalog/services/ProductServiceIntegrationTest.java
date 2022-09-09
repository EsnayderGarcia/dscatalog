package com.snayder.dscatalog.services;

import com.snayder.dscatalog.repositories.ProductRepository;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProductServiceIntegrationTest {

    private Long existingId;

    private Long nonExistingId;

    private Long totalProducts;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 1000L;
        totalProducts = 25L;
    }



    @Test
    public void deleteShouldDeleteProductWhenIdExists() {
        productService.delete(existingId);
        assertEquals(totalProducts - 1, productRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () ->
            productService.delete(nonExistingId)
        );
    }

}
