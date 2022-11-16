package com.service.order.service.file;

import com.service.order.dto.order.OrderDto;
import com.service.order.entity.file.OrderFile;
import com.service.order.error.exception.file.FileServiceException;
import com.service.order.input.order.OrderInput;
import com.service.order.input.order.OrderUpdateInput;
import com.service.order.util.file.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileUtil fileUtil;

    public long saveOrder(OrderInput orderInput) throws FileServiceException {
        return fileUtil.saveOrderData(orderInput);
    }

    public long saveOrder(OrderFile orderFile) throws FileServiceException {
        return fileUtil.saveOrderData(orderFile);
    }

    public long updateOrder(OrderUpdateInput orderUpdateInput) throws Exception {
        return fileUtil.updateOrder(orderUpdateInput);
    }

    public long deleteOrder(long orderId) throws Exception {
        return fileUtil.deleteOrder(orderId);
    }

    public OrderFile findOrderById(long orderId) throws Exception {
        return fileUtil.findOrderFileById(orderId);
    }

    public List<OrderFile> findTotalOrder() throws Exception {
        return fileUtil.findTotalOrderFile();
    }

    public List<OrderFile> findPagingOrder(int page, int size) throws Exception {
        return fileUtil.findPagingOrderFiles(page, size);
    }
}
