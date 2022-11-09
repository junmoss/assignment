package com.service.order.service.lock;

import com.service.order.config.lock.LockConfig;
import com.service.order.error.exception.lock.LockServiceException;
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

    public void lock(String key) throws Exception {
        try {
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

    public void unlock(String key) {
        redissonClient.getLock(key).unlock();
    }
}
