package com.snayder.dscatalog.tests;

import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.entities.Category;
import com.snayder.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct() {
        Product product = new Product(1L, "Poco X3 PRO", "Celular excelente!", 1800.0, "https://img.com/img.png", Instant.parse("2022-08-18T10:00:00Z"));
        product.getCategories().add(new Category(2L, "Eletronics"));

        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
