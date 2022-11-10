package com.service.product.entity.product;

import com.service.product.entity.common.BaseTimeEntity;
import com.service.product.input.ProductInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    @Lob
    private String description;

    private int count;

    private Long price;

    public static Product from(ProductInput productInput) {
        return Product.builder()
                .name(productInput.getName())
                .description(productInput.getDescription())
                .count(productInput.getCount())
                .price(productInput.getPrice())
                .build();
    }
}
