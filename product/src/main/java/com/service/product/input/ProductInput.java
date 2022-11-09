package com.service.product.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInput {
    private long id;

    @NotNull
    @NotEmpty(message = "상품명은 빈 값이 올 수 없습니다.")
    @NotBlank(message = "상품명은 공백이 포함될 수 없습니다.")
    @Size(max = 64, message = "상품명은 최대 128글자 까지 작성 가능합니다.")
    private String name;

    @NotNull
    private String description;

    private int count;

    private Long price;
}
