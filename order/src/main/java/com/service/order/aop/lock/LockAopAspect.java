package com.service.order.aop.lock;

import com.service.order.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;

    @Around("@annotation(com.service.order.aop.lock.OrderLock) && args(orderUpdateInput)")
    public Object orderAroundLockByUpdateInput(
            ProceedingJoinPoint pjp,
            OrderLockInterface orderUpdateInput
    ) throws Throwable {
        // lock 취득 시도
        String key = String.valueOf(orderUpdateInput.getId());
        lockService.lock(key);

        try {
            return pjp.proceed();
        } finally {
            // lock 해제
            lockService.unlock(key);
        }
    }

    @Around("@annotation(com.service.order.aop.lock.OrderLock) && args(orderId)")
    public Object orderAroundLockByOrderId(
            ProceedingJoinPoint pjp,
            long orderId
    ) throws Throwable {
        // lock 취득 시도
        String key = String.valueOf(orderId);
        lockService.lock(key);

        try {
            return pjp.proceed();
        } finally {
            // lock 해제
            lockService.unlock(key);
        }
    }
}
