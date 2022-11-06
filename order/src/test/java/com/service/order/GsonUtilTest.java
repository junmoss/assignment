package com.service.order;

import com.service.order.dto.product.ProductDto;
import com.service.order.util.gson.GsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GsonUtilTest {
    @Test
    public void jsonParseTest() {
        String jsonString = "[" +
                "{'productId':1,'name':'jmkanmo', 'price': 25000}" +
                ",{'productId':1,'name':'sudden attack', 'price': 50000}," +
                "{'productId':1,'name':'java', 'price': 250000}," +
                "{'productId':1,'name':'tales runner', 'price': 480000}," +
                "{'productId':1,'name':'fdsfds', 'price': 9800}]";

        List<ProductDto> productDtoList = GsonUtil.parseJsonStrToProducts(jsonString);

        Assertions.assertNotNull(productDtoList);
    }
}
