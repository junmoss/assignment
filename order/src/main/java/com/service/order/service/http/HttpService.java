package com.service.order.service.http;


import com.service.order.dto.exception.ExceptionDto;
import com.service.order.error.exception.http.HttpServiceException;
import com.service.order.util.gson.GsonUtil;
import com.service.order.util.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HttpService {
    private final HttpUtil httpUtil;

    public <T> String sendProductRequest(String url, HttpMethod httpMethod, List<T> value) {
        try {
            ResponseEntity<String> response = httpUtil.sendRequest(value, url, httpMethod);

            if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new HttpServiceException(GsonUtil.parseStrToObj(response.getBody(), ExceptionDto.class).getMessage());
            }
            return response.getBody();
        } catch (RestClientException restClientException) {
            throw new HttpServiceException("서버 연결에 실패하였습니다.");
        }
    }
}
