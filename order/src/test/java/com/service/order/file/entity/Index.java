package com.service.order.file.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Index {
    private final int orderId;
    private final int offset;
    private final int length;
}
