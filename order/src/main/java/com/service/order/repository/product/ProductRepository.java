package com.service.order.repository.product;

import com.service.order.entity.rdb.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
