package com.service.product.controller;

import com.service.product.aop.ProductOrderLock;
import com.service.product.dto.product.OrderProductDto;
import com.service.product.dto.product.ProductDto;
import com.service.product.input.OrderProductInput;
import com.service.product.input.ProductInput;
import com.service.product.service.ProductService;
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
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ProductInput productInput) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.saveProduct(productInput));
    }

    @PatchMapping
    @ProductOrderLock
    public ResponseEntity<Long> update(@Valid @RequestBody ProductInput productInput) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productInput));
    }

    @PatchMapping("/order")
    @ProductOrderLock
    public ResponseEntity<List<OrderProductDto>> orderProduct(@RequestBody List<OrderProductInput> orderProductInputs) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.orderProduct(orderProductInputs));
    }

    @PatchMapping("/cancel-order")
    @ProductOrderLock
    public ResponseEntity<List<OrderProductDto>> cancelOrderProduct(@RequestBody List<OrderProductInput> orderProductInputs) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.cancelOrderProduct(orderProductInputs));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> get(@PathVariable long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductDtoById(productId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAllProductDto());
    }

    @DeleteMapping("/{productId}")
    @ProductOrderLock
    public ResponseEntity<Long> deleteProduct(@PathVariable long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.deleteProductById(productId));
    }

    @GetMapping("/paging")
    public ResponseEntity<List<ProductDto>> getPagination(
            @RequestParam(value = "page", required = false, defaultValue = "5") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductPagination(PageRequest.of(page, size, Sort.by("createdTime"))));
    }
}
