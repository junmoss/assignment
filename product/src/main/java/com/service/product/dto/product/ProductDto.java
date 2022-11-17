package com.service.product.dto.product;

import com.service.product.entity.file.ProductFile;
import com.service.product.entity.rdb.product.Product;
import com.service.product.input.OrderProductInput;
import com.service.product.util.Util;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private final Long productId;
    private final String name;
    private final String description;
    private final Long price;
    private final int count;
    private final String createdTime;
    private final String updatedTime;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .count(product.getCount())
                .createdTime(Util.formatLocalDateTimeToStr(product.getCreatedTime()))
                .updatedTime(Util.formatLocalDateTimeToStr(product.getUpdatedTime()))
                .build();
    }

    public static ProductDto from(ProductFile productFile) {
        return ProductDto.builder()
                .productId(productFile.getProductId())
                .name(productFile.getName())
                .description(productFile.getDescription())
                .price(productFile.getPrice())
                .count(productFile.getCount())
                .createdTime(productFile.getCreatedTime())
                .updatedTime(productFile.getUpdatedTime())
                .build();
    }

    public static ProductDto from(ProductFile productFile, OrderProductInput orderProductInput) {
        return ProductDto.builder()
                .productId(productFile.getProductId())
                .name(productFile.getName())
                .description(productFile.getDescription())
                .price(productFile.getPrice() * orderProductInput.getCount())
                .count(orderProductInput.getCount())
                .createdTime(productFile.getCreatedTime())
                .updatedTime(productFile.getUpdatedTime())
                .build();
    }
}
