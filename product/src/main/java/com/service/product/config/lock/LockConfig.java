package com.service.product.config.lock;

import com.service.product.config.yaml.YamlPropertySourceFactory;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "classpath:application-util.yml", factory = YamlPropertySourceFactory.class)
@Data
public class LockConfig {
    @Value("${WAIT_TIME}")
    private int waitTime;

    @Value("${LEASE_TIME}")
    private int leaseTime;

    @Value("LOCK_KEY")
    private String lockKey;
}
