package com.service.order.dto.order;


import com.service.order.dto.product.ProductDto;
import com.service.order.entity.file.OrderFile;
import com.service.order.entity.rdb.order.Order;
import com.service.order.util.Util;
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
    private final String createdTime;
    private final String updatedTime;

    public static OrderDto from(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .name(order.getOrderInfo().getName())
                .address(order.getOrderInfo().getAddress())
                .request(order.getOrderInfo().getRequest())
                .products(order.getProductList().stream().map(ProductDto::from).collect(Collectors.toList()))
                .createdTime(Util.formatLocalDateTimeToStr(order.getCreatedTime()))
                .updatedTime(Util.formatLocalDateTimeToStr(order.getUpdatedTime()))
                .build();
    }

    public static OrderDto from(OrderFile orderFile, List<ProductDto> products) {
        return OrderDto.builder()
                .id(orderFile.getOrderId())
                .name(orderFile.getName())
                .address(orderFile.getAddress())
                .request(orderFile.getRequest())
                .products(products)
                .createdTime(orderFile.getCreatedTime())
                .updatedTime(orderFile.getUpdatedTime())
                .build();
    }
}
