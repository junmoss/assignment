package com.service.order.config.http;

import com.service.order.config.yaml.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "classpath:application-util.yml", factory = YamlPropertySourceFactory.class)
@Data
public class HttpConfig {
    @Value("${PRODUCT_SERVER_IP}")
    private String address;

    @Value("${PRODUCT_SERVER_PORT}")
    private int port;

    @Value("${READ_TIME_OUT}")
    private int readTimeOut;

    @Value("${CONN_TIME_OUT}")
    private int connTimeOut;

    @Value("${MAX_CONN_TOTAL}")
    private int maxConnTotal;

    @Value("${MAX_CONN_PER_ROUTE}")
    private int maxConnPerRoute;
}
