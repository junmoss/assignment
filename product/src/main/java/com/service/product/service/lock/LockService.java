package com.service.product.service.lock;

import com.service.product.config.lock.LockConfig;
import com.service.product.error.exception.lock.LockServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {
    private final RedissonClient redissonClient;
    private final LockConfig lockConfig;

    public void lock() throws Exception {
        try {
            String key = lockConfig.getLockKey();
            RLock lock = redissonClient.getLock(key);

            boolean isLock = lock.tryLock(lockConfig.getWaitTime(), lockConfig.getLeaseTime(), TimeUnit.SECONDS);

            if (!isLock) {
                log.error("Lock acquisition failed!!! key:${}, thread:${}", key, Thread.currentThread().getName());
                throw new LockServiceException("오류가 발생하여 작업에 실패하였습니다.");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void unlock() {
        redissonClient.getLock(lockConfig.getLockKey()).unlock();
    }
}
