package com.service.product.error.exception.lock;

public class LockServiceException extends RuntimeException {
    public LockServiceException(String error) {
        super(error);
    }
}
