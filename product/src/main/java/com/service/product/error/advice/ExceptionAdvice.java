package com.service.product.error.advice;

import com.service.product.dto.exception.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> exceptionHandler(Exception exception, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error("[order-exceptionHandler] Exception occurred ", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionDto.from(exception, httpServletRequest));
    }
}
