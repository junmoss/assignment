package com.service.order;

import com.service.order.input.product.ProductInput;
import com.service.order.util.http.HttpUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;

@SpringBootTest
class HttpUtilTest {
    @Autowired
    private HttpUtil httpUtil;

    @Test
    public void sendRequestTest() {
        try {
            ResponseEntity<?> response = httpUtil.sendRequest(Arrays.asList(new ProductInput(1L, 3), new ProductInput(2L, 3), new ProductInput(3L, 3)), "http://%s:%d/product/order", HttpMethod.POST);
            Assertions.assertNotNull(response);
        } catch (Exception e) {
            if (e instanceof RestClientException) {
                e.printStackTrace();
            }
        }
    }
}
