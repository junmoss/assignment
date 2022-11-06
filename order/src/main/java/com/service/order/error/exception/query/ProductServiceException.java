package com.service.order.error.exception.query;

public class ProductServiceException extends RuntimeException {
    public ProductServiceException(String error) {
        super(error);
    }
}
