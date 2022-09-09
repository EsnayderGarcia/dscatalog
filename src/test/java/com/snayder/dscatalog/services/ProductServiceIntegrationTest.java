package com.snayder.dscatalog.services;

import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.repositories.ProductRepository;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;
import com.snayder.dscatalog.tests.Factory;
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

    private ProductDTO productDTO;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 1000L;
        totalProducts = 25L;
        productDTO = Factory.createProductDTO();
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Throwable exception =  assertThrows(ResourceNotFoundException.class, () ->
                productService.update(nonExistingId, productDTO));

        assertEquals("Produto não encontrado para atualização", exception.getMessage());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = productService.update(existingId, productDTO);

        assertNotNull(result);

        assertEquals(ProductDTO.class, result.getClass());
        assertEquals(existingId, result.getId());
        assertNotEquals("The Lord of the Rings", result.getName());
    }

    @Test
    public void insertShouldReturnProductDTO() {
        productDTO.setId(null);
        ProductDTO result = productService.insert(productDTO);

        assertNotNull(result);

        assertEquals(ProductDTO.class, result.getClass());
        assertEquals(totalProducts + 1, result.getId());
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
        Throwable exception =  assertThrows(ResourceNotFoundException.class, () ->
                productService.delete(nonExistingId)
        );

        assertEquals("Produto não encontrado para exclusão", exception.getMessage());
    }

}
