package com.service.order.entity.order;


import com.service.order.entity.common.BaseTimeEntity;
import com.service.order.entity.product.Product;
import com.service.order.input.order.OrderInput;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "order")
    private List<Product> productList;

    @Embedded
    private OrderInfo orderInfo;

    public static Order from(OrderInput orderInput) {
        return Order.builder()
                .orderInfo(OrderInfo.from(orderInput))
                .build();
    }
}
