package com.service.order.entity.file;

import com.service.order.input.order.OrderInput;
import com.service.order.util.Util;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderFile {
    private Long orderId;
    private String name;
    private String address;
    private String request;
    private final String createdTime;
    private String updatedTime;
    private final List<ProductFile> productFiles;

    public static OrderFile from(OrderInput orderInput) {
        return OrderFile.builder()
                .orderId(orderInput.getId())
                .name(orderInput.getName())
                .address(orderInput.getAddress())
                .request(orderInput.getRequest())
                .createdTime(Util.formatLocalDateTimeToStr(LocalDateTime.now()))
                .updatedTime(Util.formatLocalDateTimeToStr(LocalDateTime.now()))
                .productFiles(orderInput.getProductInputs().stream().map(ProductFile::from).collect(Collectors.toList()))
                .build();
    }
}
