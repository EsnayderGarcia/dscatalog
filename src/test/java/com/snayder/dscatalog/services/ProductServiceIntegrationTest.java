package com.snayder.dscatalog.services;

import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.repositories.ProductRepository;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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
    public void findAllPagedShouldReturnPage() {
        PageRequest page = PageRequest.of(0, 5);
        Page<ProductDTO> result = productService.findAllPaged(page);

        assertNotNull(result);

        assertEquals(0, result.getNumber());
        assertEquals(5, result.getSize());
        assertEquals(totalProducts, result.getTotalElements());
        assertEquals(ProductDTO.class, result.getContent().get(0).getClass());
    }

    @Test
    public void findAllPagedShouldBeEmptyWhenPageDoesNotExist() {
        int nonExistingPage = 100;

        PageRequest page = PageRequest.of(nonExistingPage, 5);
        Page<ProductDTO> result = productService.findAllPaged(page);

        assertTrue(result.isEmpty());
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
