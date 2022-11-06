package com.service.order.error.exception.query;

public class OrderServiceException extends RuntimeException {
    public OrderServiceException(String error) {
        super(error);
    }
}
