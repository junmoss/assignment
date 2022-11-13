package com.service.order.repository.order;


import com.service.order.entity.rdb.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findOrderBy(Pageable pageable);

    List<Order> findAll();
}
