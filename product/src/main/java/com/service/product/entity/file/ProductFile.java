package com.service.product.entity.file;

import com.service.product.input.ProductInput;
import com.service.product.util.Util;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductFile {
    private Long productId;
    private String name;
    private String description;
    private int count;
    private Long price;
    private final String createdTime;
    private String updatedTime;

    public static ProductFile from(ProductInput productInput) {
        return ProductFile.builder()
                .productId(productInput.getId())
                .name(productInput.getName())
                .description(productInput.getDescription())
                .count(productInput.getCount())
                .price(productInput.getPrice())
                .createdTime(Util.formatLocalDateTimeToStr(LocalDateTime.now()))
                .updatedTime(Util.formatLocalDateTimeToStr(LocalDateTime.now()))
                .build();
    }
}
