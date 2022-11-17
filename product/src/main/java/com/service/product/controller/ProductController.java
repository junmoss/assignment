package com.service.product.controller;

import com.service.product.aop.ProductOrderLock;
import com.service.product.dto.product.ProductDto;
import com.service.product.input.OrderProductInput;
import com.service.product.input.ProductInput;
import com.service.product.service.product.file.FileProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final FileProductService fileProductService;

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ProductInput productInput) {
        return ResponseEntity.status(HttpStatus.OK).body(fileProductService.saveProduct(productInput));
    }

    @PatchMapping
    @ProductOrderLock
    public ResponseEntity<Long> update(@Valid @RequestBody ProductInput productInput) {
        return ResponseEntity.status(HttpStatus.OK).body(fileProductService.updateProduct(productInput));
    }

    @PatchMapping("/order")
    @ProductOrderLock
    public ResponseEntity<List<Long>> orderProduct(@RequestBody List<OrderProductInput> orderProductInputs) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(fileProductService.orderProduct(orderProductInputs));
    }

    @PatchMapping("/cancel-order")
    @ProductOrderLock
    public ResponseEntity<List<Long>> cancelOrderProduct(@RequestBody List<OrderProductInput> orderProductInputs) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(fileProductService.cancelOrderProduct(orderProductInputs));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> get(@PathVariable long productId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(fileProductService.findProductDtoById(productId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAll() throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(fileProductService.findTotalProductDto());
    }

    @DeleteMapping("/{productId}")
    @ProductOrderLock
    public ResponseEntity<Long> deleteProduct(@PathVariable long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(fileProductService.deleteProduct(productId));
    }

    @GetMapping("/paging")
    public ResponseEntity<List<ProductDto>> getPagination(
            @RequestParam(value = "page", required = false, defaultValue = "5") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(fileProductService.findPagingProductDto(page, size));
    }

    @PostMapping("/list")
    public ResponseEntity<List<ProductDto>> getPagination(@RequestBody List<OrderProductInput> orderProductInputs) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(fileProductService.findProductByJsonStr(orderProductInputs));
    }
}
