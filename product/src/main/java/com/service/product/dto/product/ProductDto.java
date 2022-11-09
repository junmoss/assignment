package com.service.product.dto.product;

import com.service.product.entity.product.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private final Long productId;
    private final String name;
    private final Long price;
    private final int count;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .count(product.getCount())
                .build();
    }
}
