package com.service.product.entity.file;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndexFile {
    private long productId;
    private int offset;
    private int length;
}

