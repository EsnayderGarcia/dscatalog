package com.snayder.dscatalog.repositories;

import com.snayder.dscatalog.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.snayder.dscatalog.entities.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT DISTINCT obj FROM Product obj " +
            "INNER JOIN obj.categories cats " +
            "WHERE (COALESCE(:categories) IS NULL OR cats IN :categories) " +
            "AND (UPPER(obj.name) LIKE CONCAT('%', UPPER(:name), '%')) " +
            "AND ((:minPrice = 0.0 AND :maxPrice = 0.0) OR obj.price BETWEEN :minPrice AND :maxPrice)")
    Page<Product> find(String name, List<Category> categories,  double minPrice, double maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.categories WHERE p IN :products")
    List<Product> findProductsAndCategories(Set<Product> products);
}
