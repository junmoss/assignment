package com.service.order.dto.order;


import com.service.order.dto.product.ProductDto;
import com.service.order.entity.order.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderDto {
    private final Long id;
    private final String name;
    private final String address;
    private final String request;
    private final List<ProductDto> products;

    public static OrderDto from(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .name(order.getOrderInfo().getName())
                .address(order.getOrderInfo().getAddress())
                .request(order.getOrderInfo().getRequest())
                .products(order.getProductList().stream().map(ProductDto::from).collect(Collectors.toList()))
                .build();
    }
}
