package com.service.product.repository;

import com.service.product.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findProductByProductId(Long ProductId, Pageable pageable);

    List<Product> findAll();
}
