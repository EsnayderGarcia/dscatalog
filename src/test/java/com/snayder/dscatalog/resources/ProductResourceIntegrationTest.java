package com.snayder.dscatalog.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private Long totalProducts;

    private Long existingId;

    private Long nonExistingId;

    private ProductDTO productDTO;

    @BeforeEach
    void setup() {
        totalProducts = 25L;
        existingId = 1L;
        nonExistingId = 1000L;
        productDTO = Factory.createProductDTO();
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByIncome() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/products?size=5&page=0&sort=price,desc")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.content").exists());

        result.andExpect(jsonPath("$.size").value(5));
        result.andExpect(jsonPath("$.number").value(0));
        result.andExpect(jsonPath("$.totalPages").value(5));
        result.andExpect(jsonPath("$.totalElements").value(totalProducts));

        result.andExpect(jsonPath("$.content[0].price").value(4170.0));
        result.andExpect(jsonPath("$.content[1].price").value(2350.0));
        result.andExpect(jsonPath("$.content[2].price").value(2340.0));
        result.andExpect(jsonPath("$.content[3].price").value(2250.0));
        result.andExpect(jsonPath("$.content[4].price").value(2250.0));
    }

    @Test
    public void findAllShouldReturnEmptyWhenPageDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/products?page=1000")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();
        Double expectedPrice = productDTO.getPrice();

        ResultActions result = mockMvc
                .perform(put("/products/{idProduct}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.description").value(expectedDescription));
        result.andExpect(jsonPath("$.price").value(expectedPrice));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(put("/products/{idProduct}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.error")
                .value("Produto não encontrado para atualização"));
    }

    @Test
    public void deleteShouldDeleteWhenIdExists() throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/products/{idProduct}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/products/{idProduct}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.error")
                .value("Produto não encontrado para exclusão"));
    }
}
