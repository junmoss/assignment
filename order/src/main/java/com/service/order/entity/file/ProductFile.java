package com.service.order.entity.file;

import com.service.order.input.product.ProductInput;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductFile {
    private final long id;
    private final int count;

    public static ProductFile from(ProductInput productInput) {
        return ProductFile.builder()
                .id(productInput.getId())
                .count(productInput.getCount())
                .build();
    }
}
