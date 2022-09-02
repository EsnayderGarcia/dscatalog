package com.snayder.dscatalog.resources;

import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.services.ProductService;
import com.snayder.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductDTO productDTO;

    PageImpl<ProductDTO> page;

    @BeforeEach
    void setup() {
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(productService.findAllPaged(any())).thenReturn(page);
    }

    @Test
    public void findAllShouldReturnHttpStatusOk() throws Exception{
        ResultActions result = mockMvc.perform(get("/products"));

        verify(productService).findAllPaged(any());
        result.andExpect(status().isOk());
    }
}
