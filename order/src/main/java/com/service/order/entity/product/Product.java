package com.service.order.entity.product;

import com.service.order.dto.product.ProductDto;
import com.service.order.entity.common.BaseTimeEntity;
import com.service.order.entity.order.Order;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String name;

    private int count;

    private Long price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public static Product from(ProductDto productDto) {
        return Product.builder()
                .productId(productDto.getProductId())
                .name(productDto.getName())
                .count(productDto.getCount())
                .price(productDto.getPrice())
                .build();
    }
}
