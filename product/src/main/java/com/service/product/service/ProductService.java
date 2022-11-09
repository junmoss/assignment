package com.service.product.service;

import com.service.product.dto.product.OrderProductDto;
import com.service.product.dto.product.ProductDto;
import com.service.product.entity.product.Product;
import com.service.product.error.exception.query.ProductServiceException;
import com.service.product.input.OrderProductInput;
import com.service.product.input.ProductInput;
import com.service.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public long saveProduct(ProductInput productInput) {
        try {
            return productRepository.save(Product.from(productInput)).getId();
        } catch (ProductServiceException e) {
            throw new ProductServiceException("상품 데이터 저장에 실패하였습니다.");
        }
    }

    @Transactional
    public long deleteProductById(long productId) {
        try {
            productRepository.deleteById(productId);
            return productId;
        } catch (ProductServiceException e) {
            throw new ProductServiceException("상품 데이터 삭제에 실패하였습니다.");
        }
    }

    @Transactional
    public long updateProduct(ProductInput productInput) {
        try {
            Product product = findProductById(productInput.getId());
            product.setName(productInput.getName());
            product.setDescription(productInput.getDescription());
            product.setPrice(productInput.getPrice());
            product.setCount(productInput.getCount());
            return product.getId();
        } catch (ProductServiceException e) {
            throw new ProductServiceException("상품 데이터 수정에 실패하였습니다.");
        }
    }

    @Transactional
    public List<OrderProductDto> orderProduct(List<OrderProductInput> orderProductInputs) {
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();

        try {
            for (OrderProductInput orderProductInput : orderProductInputs) {
                Product product = findProductById(orderProductInput.getId());

                if (orderProductInput.getCount() > product.getCount()) {
                    throw new RuntimeException();
                } else {
                    product.setCount(product.getCount() - orderProductInput.getCount());
                }
                productRepository.save(product); // 생략 가능하면 생략하도록
                orderProductDtoList.add(OrderProductDto.from(product));
            }
        } catch (ProductServiceException e) {
            throw new ProductServiceException("상품 주문 진행에 실패하였습니다.");
        }
        return orderProductDtoList;
    }

    @Transactional
    public List<OrderProductDto> cancelOrderProduct(List<OrderProductInput> orderProductInputs) {
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();

        try {
            for (OrderProductInput orderProductInput : orderProductInputs) {
                Product product = findProductById(orderProductInput.getId());
                product.setCount(product.getCount() + orderProductInput.getCount());
                productRepository.save(product); // 생략 가능하면 생략하도록
                orderProductDtoList.add(OrderProductDto.from(product));
            }
        } catch (ProductServiceException e) {
            throw new ProductServiceException("상품 주문 진행에 실패하였습니다.");
        }
        return orderProductDtoList;
    }

    @Transactional(readOnly = true)
    public Product findProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductServiceException("상품 정보를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public ProductDto findProductDtoById(long id) {
        return ProductDto.from(findProductById(id));
    }

    @Transactional(readOnly = true)
    public List<ProductDto> findAllProductDto() {
        return productRepository.findAll().stream().map(ProductDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> findProductPagination(long productId, Pageable pageable) {
        return productRepository.findProductByProductId(productId, pageable).getContent().stream().map(ProductDto::from).collect(Collectors.toList());
    }
}
