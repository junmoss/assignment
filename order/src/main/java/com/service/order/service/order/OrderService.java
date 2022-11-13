package com.service.order.service.order;

import com.service.order.dto.order.OrderDto;
import com.service.order.entity.rdb.product.Product;
import com.service.order.error.exception.query.OrderServiceException;
import com.service.order.error.exception.query.ProductServiceException;
import com.service.order.input.order.OrderInput;
import com.service.order.input.order.OrderUpdateInput;
import com.service.order.input.product.ProductInput;
import com.service.order.service.query.QueryService;
import com.service.order.service.http.HttpService;
import com.service.order.util.gson.GsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final QueryService queryService;
    private final HttpService httpService;

    public long createOrder(OrderInput orderInput) {
        List<Product> products = GsonUtil.parseJsonStrToProducts(httpService.sendProductRequest("http://%s:%d/product/order", HttpMethod.PATCH, orderInput.getProductInputs())).stream().map(Product::from).collect(Collectors.toList());

        try {
            return queryService.order(orderInput, products);
        } catch (Exception e) {
            if (e instanceof OrderServiceException
                    || e instanceof ProductServiceException) {
                httpService.sendProductRequest("http://%s:%d/product/cancel-order", HttpMethod.PATCH, orderInput.getProductInputs());
            }
            throw e;
        }
    }

    public long updateOrder(OrderUpdateInput orderUpdateInput) {
        return queryService.updateOrder(orderUpdateInput);
    }

    public long cancelOrder(long orderId) {
        List<ProductInput> productInputs = queryService.findOrderProductInputsById(orderId);
        httpService.sendProductRequest("http://%s:%d/product/cancel-order", HttpMethod.PATCH, productInputs);

        try {
            queryService.cancelOrder(orderId, productInputs.stream().map(product -> product.getId()).collect(Collectors.toList()));
        } catch (Exception e) {
            if (e instanceof OrderServiceException
                    || e instanceof ProductServiceException) {
                httpService.sendProductRequest("http://%s:%d/product/order", HttpMethod.PATCH, productInputs);
            }
            throw e;
        }
        return orderId;
    }

    public OrderDto findOrder(long orderId) {
        return queryService.findOrderDtoById(orderId);
    }

    public List<OrderDto> findAllOrder() {
        return queryService.findAllOrderDto();
    }

    public List<OrderDto> findOrderPagination(Pageable pageable) {
        return queryService.findOrderPaginationBy(pageable);
    }
}
