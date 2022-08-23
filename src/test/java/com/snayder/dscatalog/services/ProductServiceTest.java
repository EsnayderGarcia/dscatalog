package com.snayder.dscatalog.services;

import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.entities.Product;
import com.snayder.dscatalog.repositories.ProductRepository;
import com.snayder.dscatalog.services.exceptions.DataBaseException;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;
import com.snayder.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private long existingId;

    private long nonExistingId;

    private long dependentId;

    private Product product;

    private ProductDTO productDTO;

    private PageImpl<Product> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));

        Mockito.doNothing()
                .when(productRepository)
                .deleteById(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class)
                .when(productRepository)
                .deleteById(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class)
                .when(productRepository)
                .deleteById(dependentId);

        Mockito.when(productRepository
                .findAll((Pageable) ArgumentMatchers.any()))
                .thenReturn(page);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));

        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.getOne(existingId)).thenReturn(product);

        Mockito.doThrow(EntityNotFoundException.class)
                .when(productRepository)
                .getOne(nonExistingId);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = this.productService.update(existingId, productDTO);

        Mockito.verify(productRepository).getOne(existingId);
        Mockito.verify(productRepository).save(ArgumentMatchers.any());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(ProductDTO.class, result.getClass());
        Assertions.assertEquals(existingId, result.getId());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.productService.update(nonExistingId, productDTO);
        });
    }

    @Test
    public void insertShouldReturnProductDTO() {
        productDTO.setId(null);
        ProductDTO result = this.productService.insert(productDTO);

        Mockito.verify(productRepository).save(ArgumentMatchers.any());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(ProductDTO.class, result.getClass());
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    public void findBydIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = this.productService.findById(existingId);

        Mockito.verify(productRepository).findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(ProductDTO.class, result.getClass());
        Assertions.assertEquals(existingId, result.getId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.productService.findById(nonExistingId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ProductDTO> result = this.productService.findAllPaged(pageable);

        Mockito.verify(productRepository).findAll(pageable);
        Assertions.assertNotNull(result);
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