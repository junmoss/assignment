package com.service.order.entity.file;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Index {
    private int orderId;
    private int offset;
    private int length;
}

