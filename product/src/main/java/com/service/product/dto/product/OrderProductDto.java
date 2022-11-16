package com.service.product.dto.product;

import com.service.product.entity.rdb.product.Product;
import com.service.product.input.OrderProductInput;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProductDto {
    private final Long productId;
    private final String name;
    private final Long price;
    private final int count;

    public static OrderProductDto from(Product product, OrderProductInput orderProductInput) {
        return OrderProductDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .count(orderProductInput.getCount())
                .build();
    }
}
