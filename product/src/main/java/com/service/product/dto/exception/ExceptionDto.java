package com.service.product.dto.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Data
@Builder
public class ExceptionDto {
    private final String message;
    private final int statusCode;
    private String method;
    private String contentType;
    private final String url;

    public static ExceptionDto from(Exception exception, HttpServletRequest httpServletRequest) {
        return ExceptionDto.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .url(httpServletRequest.getRequestURI())
                .method(httpServletRequest.getMethod())
                .contentType(httpServletRequest.getContentType())
                .build();
    }
}
