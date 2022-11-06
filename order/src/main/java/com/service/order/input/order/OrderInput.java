package com.service.order.input.order;


import com.service.order.input.product.ProductInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInput {
    private Long id;

    @NotNull
    @NotEmpty(message = "주문자 명은 빈 값이 올 수 없습니다.")
    @NotBlank(message = "주문자 명은 공백이 포함될 수 없습니다.")
    @Size(max = 64, message = "주문자 명은 최대 64글자 까지 작성 가능합니다.")
    private String name;

    @NotNull
    @NotEmpty(message = "주소는 빈 값이 올 수 없습니다.")
    @Size(max = 255, message = "주문 주소는 최대 255글자 까지 작성 가능합니다.")
    private String address;

    @Size(max = 255, message = "요청사항은 최대 255글자 까지 작성 가능합니다.")
    private String request;

    @Size(max = 10, message = "상품은 최대 10개의 종류만 선택 가능합니다.")
    private List<ProductInput> productInputs;
}
