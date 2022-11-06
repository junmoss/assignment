package com.service.order.input.product;

import com.service.order.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInput {
    private Long id;
    private int count;

    public static ProductInput from(Product product) {
        return ProductInput.builder()
                .id(product.getProductId())
                .count(product.getCount())
                .build();
    }
}
