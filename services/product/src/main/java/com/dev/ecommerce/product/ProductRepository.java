package com.dev.ecommerce.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
   // List<Product> findAllByIdOrderById(List<Integer> productIds);
    List<Product> findAllByIdInOrderById(List<Integer> ids);
}
