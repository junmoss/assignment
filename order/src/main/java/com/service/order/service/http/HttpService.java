package com.service.order.service.http;


import com.service.order.dto.exception.ExceptionDto;
import com.service.order.error.exception.http.HttpServiceException;
import com.service.order.util.gson.GsonUtil;
import com.service.order.util.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HttpService {
    private final HttpUtil httpUtil;

    // body 에 데이터를 담아 HTTP 요청 전송 (POST,PUT,PATCH)
    public <T> String sendProductWriteRequest(String url, HttpMethod httpMethod, List<T> value) {
        try {
            ResponseEntity<String> response = httpUtil.sendWriteRequest(value, url, httpMethod);
            return response.getBody();
        } catch (RestClientException restClientException) {
            if (restClientException instanceof HttpServerErrorException) {
                throw new HttpServiceException(GsonUtil.parseStrToObj(((HttpServerErrorException) restClientException).getResponseBodyAsString(), ExceptionDto.class).getMessage());
            }
            throw restClientException;
        }
    }

    // URL(query param 기반) HTTP 요청 전송 (GET)
    public String sendProductReadRequest(String url) {
        try {
            ResponseEntity<String> response = httpUtil.sendReadRequest(url);
            return response.getBody();
        } catch (RestClientException restClientException) {
            if (restClientException instanceof HttpServerErrorException) {
                throw new HttpServiceException(GsonUtil.parseStrToObj(((HttpServerErrorException) restClientException).getResponseBodyAsString(), ExceptionDto.class).getMessage());
            }
            throw restClientException;
        }
    }
}
