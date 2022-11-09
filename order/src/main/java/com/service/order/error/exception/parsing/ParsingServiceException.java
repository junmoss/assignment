package com.service.order.error.exception.parsing;

public class ParsingServiceException extends RuntimeException {
    public ParsingServiceException(String error) {
        super(error);
    }
}
