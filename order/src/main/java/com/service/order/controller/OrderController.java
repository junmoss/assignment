package com.service.order.controller;

import com.service.order.dto.order.OrderDto;
import com.service.order.input.order.OrderInput;
import com.service.order.input.order.OrderUpdateInput;
import com.service.order.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody OrderInput orderInput) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.createOrder(orderInput));
    }

    @PatchMapping
    public ResponseEntity<Long> update(@Valid @RequestBody OrderUpdateInput orderUpdateInput) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(orderUpdateInput));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Long> delete(@PathVariable long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancelOrder(orderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> get(@PathVariable long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrder(orderId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllOrder());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDto>> getPagination(
            @PathVariable long orderId,
            @RequestParam(value = "page", required = false, defaultValue = "5") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrderPagination(orderId, PageRequest.of(page, size, Sort.by("createdTime"))));
    }
}
