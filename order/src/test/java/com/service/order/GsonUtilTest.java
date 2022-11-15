package com.service.order;

import com.service.order.dto.product.ProductDto;
import com.service.order.entity.file.OrderFile;
import com.service.order.input.order.OrderInput;
import com.service.order.input.product.ProductInput;
import com.service.order.util.gson.GsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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

    @Test
    public void parseObjToStrTest() {
        OrderInput orderInput = new OrderInput();
        orderInput.setId(25L);
        orderInput.setName("강준모");
        orderInput.setAddress("강서구등촌동");
        orderInput.setRequest("조심히 천천히 와주세요.");
        List<ProductInput> productInputs = new ArrayList<>();
        productInputs.add(new ProductInput(3L, 4));
        productInputs.add(new ProductInput(2L, 8));
        productInputs.add(new ProductInput(1L, 12));
        orderInput.setProductInputs(productInputs);
        OrderFile orderFile = OrderFile.from(orderInput);
        String orderFileJsonStr = GsonUtil.parseObjToStr(orderFile);
        OrderFile convertedOrderFile = GsonUtil.parseStrToObj(orderFileJsonStr, OrderFile.class);
        Assertions.assertNotNull(orderFileJsonStr);
        Assertions.assertNotNull(convertedOrderFile);
    }
}
