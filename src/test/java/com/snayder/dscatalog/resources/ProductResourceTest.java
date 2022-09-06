package com.snayder.dscatalog.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.services.ProductService;
import com.snayder.dscatalog.services.exceptions.DataBaseException;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;
import com.snayder.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductResource.class)
public class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProductService productService;

    private ProductDTO productDTO;

    private PageImpl<ProductDTO> page;

    private Long existingId;

    private Long nonExistingId;

    private Long dependentId;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(productService.insert(any())).thenReturn(productDTO);

        doThrow(ResourceNotFoundException.class)
                .when(productService).delete(nonExistingId);

        doThrow(DataBaseException.class).when(productService).delete(dependentId);

        doNothing().when(productService).delete(existingId);

        when(productService.update(eq(existingId), any())).thenReturn(productDTO);

        doThrow(ResourceNotFoundException.class)
                .when(productService).update(eq(nonExistingId), any());

        when(productService.findById(existingId)).thenReturn(productDTO);

        doThrow(ResourceNotFoundException.class)
                .when(productService).findById(nonExistingId);

        when(productService.findAllPaged(any())).thenReturn(page);
    }

    @Test
    public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/products/{idProduct}", dependentId)
                .accept(MediaType.APPLICATION_JSON));

        verify(productService).delete(dependentId);
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/products/{idProduct}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        verify(productService).delete(nonExistingId);
        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/products/{idProduct}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        verify(productService).delete(existingId);
        result.andExpect(status().isNoContent());
    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        verify(productService).insert(any());

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.imgUrl").exists());
    }

    @Test
    public void updateShouldNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(put("/products/{idProduct}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        verify(productService).update(eq(nonExistingId), any());
        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        /*Converte o ProductDTO para um formato Json!*/
        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(put("/products/{idProduct}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        verify(productService).update(eq(existingId), any());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.imgUrl").exists());
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
    public void findAllShouldReturnStatusCodeOk() throws Exception {
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        verify(productService).findAllPaged(any());
        result.andExpect(status().isOk());
    }

}
