package com.service.order.util.file;

import com.service.order.entity.file.IndexFile;
import com.service.order.entity.file.OrderFile;
import com.service.order.error.exception.file.FileServiceException;
import com.service.order.input.order.OrderInput;
import com.service.order.input.order.OrderUpdateInput;
import com.service.order.util.Util;
import com.service.order.util.gson.GsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileUtil extends FileCommonUtil {
    public long saveOrderData(OrderInput orderInput) throws FileServiceException {
        OrderFile orderFile = OrderFile.from(orderInput);
        orderFile.setOrderId(getNextOrderId());

        String orderJsonStr = GsonUtil.parseObjToStr(orderFile);
        long orderId = orderFile.getOrderId();
        int offset = writeOrderText(orderJsonStr);
        int length = orderJsonStr.getBytes(StandardCharsets.UTF_8).length;

        writeIndexText(GsonUtil.parseObjToStr(
                IndexFile.builder()
                        .orderId(orderId)
                        .offset(offset)
                        .length(length)
                        .build()) + "\n");
        return orderId;
    }

    public long saveOrderData(OrderFile orderFile) throws FileServiceException {
        orderFile.setOrderId(getNextOrderId());
        String orderJsonStr = GsonUtil.parseObjToStr(orderFile);
        long orderId = orderFile.getOrderId();
        int offset = writeOrderText(orderJsonStr);
        int length = orderJsonStr.getBytes(StandardCharsets.UTF_8).length;

        writeIndexText(GsonUtil.parseObjToStr(
                IndexFile.builder()
                        .orderId(orderId)
                        .offset(offset)
                        .length(length)
                        .build()) + "\n");
        return orderId;
    }

    public long updateOrder(OrderUpdateInput orderUpdateInput) throws Exception {
        IndexFile index = readIndexDataById(orderUpdateInput.getId());
        OrderFile updateOrderData = readOrderDataByIndex(index);

        updateOrderData.setName(orderUpdateInput.getName());
        updateOrderData.setAddress(orderUpdateInput.getAddress());
        updateOrderData.setRequest(orderUpdateInput.getRequest());
        updateOrderData.setUpdatedTime(Util.formatLocalDateTimeToStr(LocalDateTime.now()));

        IndexFile repIndex = IndexFile.builder()
                .orderId(updateOrderData.getOrderId())
                .offset(index.getOffset())
                .length(GsonUtil.parseObjToStr(updateOrderData).getBytes(StandardCharsets.UTF_8).length)
                .build();

        updateIndexDataText(repIndex.getOrderId(), repIndex);
        updateOrderDataText(repIndex.getOffset(), index.getLength(), GsonUtil.parseObjToStr(updateOrderData));
        return orderUpdateInput.getId();
    }

    public long deleteOrder(long orderId) throws Exception {
        IndexFile deleteIndex = deleteIndexData(orderId);
        deleteOrderData(deleteIndex);
        return orderId;
    }

    public List<OrderFile> findTotalOrderFile() throws Exception {
        String[] parsed = readAllText("index.txt");
        List<OrderFile> orderFiles = new ArrayList<>();

        for (String parse : parsed) {
            orderFiles.add(readOrderDataByIndex(GsonUtil.parseStrToObj(parse, IndexFile.class)));
        }
        return orderFiles;
    }

    public OrderFile findOrderFileById(long orderId) throws Exception {
        return readOrderDataByIndex(readIndexDataById(orderId));
    }

    public List<OrderFile> findPagingOrderFiles(int page, int size) throws Exception {
        List<IndexFile> indexList = readIndexPagingById(page, size);
        List<OrderFile> orderDataList = new ArrayList<>();

        for (IndexFile index : indexList) {
            orderDataList.add(readOrderDataByIndex(index));
        }
        return orderDataList;
    }
}
