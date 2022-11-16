package com.service.product.service.product.file;

import com.service.product.dto.product.OrderProductDto;
import com.service.product.dto.product.ProductDto;
import com.service.product.entity.file.ProductFile;
import com.service.product.entity.rdb.product.Product;
import com.service.product.error.exception.query.ProductServiceException;
import com.service.product.input.OrderProductInput;
import com.service.product.input.ProductInput;
import com.service.product.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileProductService {
    private final FileService fileService;

    public long saveProduct(ProductInput productInput) {
        try {
            return fileService.saveProduct(productInput);
        } catch (Exception e) {
            throw new ProductServiceException("상품 데이터 저장에 실패하엿습니다.");
        }
    }

    public long deleteProduct(long productId) {
        try {
            return fileService.deleteProduct(productId);
        } catch (Exception e) {
            throw new ProductServiceException("상품 데이터 삭제에 실패하였습니다.");
        }
    }

    public long updateProduct(ProductInput productInput) {
        try {
            return fileService.updateProduct(productInput);
        } catch (Exception e) {
            throw new ProductServiceException("상품 데이터 수정에 실패하였습니다.");
        }
    }

    public List<Long> orderProduct(List<OrderProductInput> orderProductInputs) throws Exception {
        List<Long> orderProductIds = new ArrayList<>();

        for (OrderProductInput orderProductInput : orderProductInputs) {
            ProductFile productFile = fileService.findProductById(orderProductInput.getId());

            if (productFile.getCount() <= 0) {
                continue;
            }

            if (orderProductInput.getCount() > productFile.getCount()) {
                throw new ProductServiceException("상품 주문 진행에 실패하였습니다.");
            } else {
                productFile.setCount(productFile.getCount() - orderProductInput.getCount());
            }
            orderProductIds.add(fileService.saveProduct(productFile));
        }
        return orderProductIds;
    }

    public List<Long> cancelOrderProduct(List<OrderProductInput> orderProductInputs) throws Exception {
        List<Long> cancelProductIds = new ArrayList<>();

        try {
            for (OrderProductInput orderProductInput : orderProductInputs) {
                ProductFile productFile = fileService.findProductById(orderProductInput.getId());
                productFile.setCount(productFile.getCount() + orderProductInput.getCount());
                cancelProductIds.add(fileService.saveProduct(productFile));
            }
        } catch (ProductServiceException e) {
            throw new ProductServiceException("상품 주문 취소에 실패하였습니다.");
        }
        return cancelProductIds;
    }

    public ProductDto findProductDtoById(long productId) throws Exception {
        return ProductDto.from(fileService.findProductById(productId));
    }

    public List<ProductDto> findTotalProductDto() throws Exception {
        return fileService.findTotalProduct().stream().map(ProductDto::from).collect(Collectors.toList());
    }

    public List<ProductDto> findPagingProductDto(int page, int size) throws Exception {
        return fileService.findProductPaging(page, size).stream().map(ProductDto::from).collect(Collectors.toList());
    }
}
