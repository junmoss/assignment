package com.service.order.service.order.file;

import com.service.order.dto.order.OrderDto;
import com.service.order.dto.product.ProductDto;
import com.service.order.entity.file.OrderFile;
import com.service.order.error.exception.file.FileServiceException;
import com.service.order.input.order.OrderInput;
import com.service.order.input.order.OrderUpdateInput;
import com.service.order.service.file.FileService;
import com.service.order.service.http.HttpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileOrderService {
    private final HttpService httpService;
    private final FileService fileService;

    public long createOrder(OrderInput orderInput) throws Exception {
        httpService.sendProductRequest("http://%s:%d/product/order", HttpMethod.PATCH, orderInput.getProductInputs());
        long orderId = 0;

        try {
            orderId = fileService.saveOrder(orderInput);
            return orderId;
        } catch (Exception e) {
            if (e instanceof FileServiceException) {
                fileService.deleteOrder(orderId);
                httpService.sendProductRequest("http://%s:%d/product/cancel-order", HttpMethod.PATCH, orderInput.getProductInputs());
            }
            throw e;
        }
    }

    public long cancelOrder(long orderId) throws Exception {
        OrderFile orderFile = fileService.findOrderById(orderId);
        httpService.sendProductRequest("http://%s:%d/product/cancel-order", HttpMethod.PATCH, orderFile.getProductFiles());

        try {
            fileService.deleteOrder(orderId);
        } catch (Exception e) {
            if (e instanceof FileServiceException) {
                fileService.saveOrder(orderFile);
                httpService.sendProductRequest("http://%s:%d/product/order", HttpMethod.PATCH, orderFile.getProductFiles());
            }
            throw e;
        }
        return orderId;
    }

    public long updateOrder(OrderUpdateInput orderUpdateInput) throws Exception {
        return fileService.updateOrder(orderUpdateInput);
    }

    public OrderDto findOrderDtoById(long orderId) throws Exception {
        OrderFile orderFile = fileService.findOrderById(orderId);
        List<ProductDto> productDtoList = new ArrayList<>(); // send http request to product server and get product list
        return OrderDto.from(orderFile, productDtoList);
    }

    public List<OrderDto> findTotalOrder() throws Exception {
        return fileService.findTotalOrder().stream().map(orderFile -> {
            // send http request to product server and get product list
            return OrderDto.from(orderFile, new ArrayList<>());
        }).collect(Collectors.toList());
    }

    public List<OrderDto> findOrderPaging(int page, int size) throws Exception {
        return fileService.findPagingOrder(page, size).stream().map(orderFile -> {
            // send http request to product server and get product list
            return OrderDto.from(orderFile, new ArrayList<>());
        }).collect(Collectors.toList());
    }
}
