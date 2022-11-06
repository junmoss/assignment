package com.service.order.entity.order;

import com.service.order.input.order.OrderInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    private String name;
    private String address;
    private String request;

    public static OrderInfo from(OrderInput orderInput) {
        return OrderInfo.builder()
                .name(orderInput.getName())
                .address(orderInput.getAddress())
                .request(orderInput.getRequest())
                .build();
    }
}
