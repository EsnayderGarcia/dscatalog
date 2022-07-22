package com.snayder.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snayder.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
