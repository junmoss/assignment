package com.service.product.error.exception.query;

public class ProductServiceException extends RuntimeException {
    public ProductServiceException(String error) {
        super(error);
    }
}
