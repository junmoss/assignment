package com.service.order.controller;

import com.service.order.aop.lock.OrderLock;
import com.service.order.dto.order.OrderDto;
import com.service.order.input.order.OrderInput;
import com.service.order.input.order.OrderUpdateInput;
import com.service.order.service.order.file.FileOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {
    private final FileOrderService orderService;

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody OrderInput orderInput) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.createOrder(orderInput));
    }

    @PatchMapping
    @OrderLock
    public ResponseEntity<Long> update(@Valid @RequestBody OrderUpdateInput orderUpdateInput) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(orderUpdateInput));
    }

    @DeleteMapping("/{orderId}")
    @OrderLock
    public ResponseEntity<Long> delete(@PathVariable long orderId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancelOrder(orderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> get(@PathVariable long orderId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrderDtoById(orderId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAll() throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findTotalOrder());
    }

    @GetMapping("/paging")
    public ResponseEntity<List<OrderDto>> getPagination(
            @RequestParam(value = "page", required = false, defaultValue = "5") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrderPaging(page, size));
    }
}
