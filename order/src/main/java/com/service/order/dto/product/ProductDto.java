package com.service.order.dto.product;

import com.service.order.entity.rdb.product.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private final Long id;
    private final Long productId;
    private final String name;
    private final Long price;
    private final int count;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .count(product.getCount())
                .build();
    }
}
