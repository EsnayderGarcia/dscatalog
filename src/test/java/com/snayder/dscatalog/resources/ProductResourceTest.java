package com.snayder.dscatalog.resources;

import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.services.ProductService;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;
import com.snayder.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductResource.class)
public class ProductResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductDTO productDTO;

    private PageImpl<ProductDTO> page;

    private Long existingId;

    private Long nonExistingId;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 2L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        doThrow(ResourceNotFoundException.class)
                .when(productService).findById(nonExistingId);

        when(productService.findAllPaged(any())).thenReturn(page);

        when(productService.findById(existingId)).thenReturn(productDTO);
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/products/{idProduct}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        verify(productService).findById(nonExistingId);

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/products/{idProduct}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        verify(productService).findById(existingId);

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.imgUrl").exists());
    }

    @Test
    public void findAllShouldReturnHttpStatusOk() throws Exception {
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        verify(productService).findAllPaged(any());
        result.andExpect(status().isOk());
    }
}
