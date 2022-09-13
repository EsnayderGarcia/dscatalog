package com.snayder.dscatalog.resources;

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

    private Long totalProducts;

    @BeforeEach
    void setup() {
        totalProducts = 25L;
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

}
