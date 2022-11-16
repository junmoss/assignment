package com.service.order.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service.order.entity.rdb.product.Product;
import com.service.order.util.Util;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    @JsonIgnore
    private final Long id;
    private final Long productId;
    private final String name;
    private final Long price;
    private final int count;
    private final String createdTime;
    private final String updatedTime;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .count(product.getCount())
                .createdTime(Util.formatLocalDateTimeToStr(product.getCreatedTime()))
                .updatedTime(Util.formatLocalDateTimeToStr(product.getUpdatedTime()))
                .build();
    }
}
