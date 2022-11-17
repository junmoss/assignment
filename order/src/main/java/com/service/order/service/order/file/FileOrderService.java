package com.service.order.service.order.file;

import com.service.order.dto.order.OrderDto;
import com.service.order.dto.product.ProductDto;
import com.service.order.entity.file.OrderFile;
import com.service.order.error.exception.file.FileServiceException;
import com.service.order.input.order.OrderInput;
import com.service.order.input.order.OrderUpdateInput;
import com.service.order.service.file.FileService;
import com.service.order.service.http.HttpService;
import com.service.order.util.gson.GsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileOrderService {
    private final HttpService httpService;
    private final FileService fileService;

    public long createOrder(OrderInput orderInput) throws Exception {
        httpService.sendProductWriteRequest("http://%s:%d/product/order", HttpMethod.PATCH, orderInput.getProductInputs());
        long orderId = 0;

        try {
            orderId = fileService.saveOrder(orderInput);
            return orderId;
        } catch (Exception e) {
            if (e instanceof RuntimeException || e instanceof FileServiceException) {
                fileService.deleteOrder(orderId);
                httpService.sendProductWriteRequest("http://%s:%d/product/cancel-order", HttpMethod.PATCH, orderInput.getProductInputs());
            }
            throw e;
        }
    }

    public long cancelOrder(long orderId) throws Exception {
        OrderFile orderFile = fileService.findOrderById(orderId);
        httpService.sendProductWriteRequest("http://%s:%d/product/cancel-order", HttpMethod.PATCH, orderFile.getProductFiles());

        try {
            fileService.deleteOrder(orderId);
        } catch (Exception e) {
            if (e instanceof RuntimeException || e instanceof FileServiceException) {
                fileService.saveOrder(orderFile);
                httpService.sendProductWriteRequest("http://%s:%d/product/order", HttpMethod.PATCH, orderFile.getProductFiles());
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
        String resultStr = httpService.sendProductWriteRequest("http://%s:%d/product/list", HttpMethod.POST, orderFile.getProductFiles());
        return OrderDto.from(orderFile, Arrays.asList(GsonUtil.parseStrToObj(resultStr, ProductDto[].class)));
    }

    public List<OrderDto> findTotalOrder() throws Exception {
        return fileService.findTotalOrder().stream().map(orderFile -> {
            String resultStr = httpService.sendProductWriteRequest("http://%s:%d/product/list", HttpMethod.POST, orderFile.getProductFiles());
            return OrderDto.from(orderFile, Arrays.asList(GsonUtil.parseStrToObj(resultStr, ProductDto[].class)));
        }).collect(Collectors.toList());
    }

    public List<OrderDto> findOrderPaging(int page, int size) throws Exception {
        return fileService.findPagingOrder(page, size).stream().map(orderFile -> {
            String resultStr = httpService.sendProductWriteRequest("http://%s:%d/product/list", HttpMethod.POST, orderFile.getProductFiles());
            return OrderDto.from(orderFile, Arrays.asList(GsonUtil.parseStrToObj(resultStr, ProductDto[].class)));
        }).collect(Collectors.toList());
    }
}
