package com.service.order.error.exception.http;

public class HttpServiceException extends RuntimeException {
    public HttpServiceException(String error) {
        super(error);
    }
}
