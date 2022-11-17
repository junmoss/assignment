package com.service.order.util.http;

import com.service.order.config.http.HttpConfig;
import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class HttpUtil {
    private final HttpConfig httpConfig;

    public <T> ResponseEntity<String> sendWriteRequest(T obj, String url, HttpMethod httpMethod) {
        RestTemplate restTemplate = new RestTemplate(setHttpRequestFactory());
        return restTemplate.exchange(String.format(url, httpConfig.getAddress(), httpConfig.getPort()), httpMethod, new HttpEntity(obj, getHttpHeaders()), String.class);
    }

    public ResponseEntity<String> sendReadRequest(String url) {
        RestTemplate restTemplate = new RestTemplate(setHttpRequestFactory());
        return restTemplate.exchange(String.format(url, httpConfig.getAddress(), httpConfig.getPort()), HttpMethod.GET, new HttpEntity(getHttpHeaders()), String.class);
    }

    private HttpComponentsClientHttpRequestFactory setHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(httpConfig.getReadTimeOut());
        factory.setReadTimeout(httpConfig.getConnTimeOut());

        factory.setHttpClient(HttpClientBuilder.create()
                .setMaxConnTotal(httpConfig.getMaxConnTotal())
                .setMaxConnPerRoute(httpConfig.getMaxConnPerRoute())
                .build());
        return factory;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
