package com.service.order.aop;

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

    @Around("@annotation(com.service.order.aop.OrderLock) && args(orderUpdateInput,orderId)")
    public Object orderAroundLockMethod(
            ProceedingJoinPoint pjp,
            OrderLockInterface orderUpdateInput,
            long orderId
    ) throws Throwable {
        // lock 취득 시도
        String key = (orderUpdateInput == null) ? String.valueOf(orderId) : String.valueOf(orderUpdateInput.getId());
        lockService.lock(key);

        try {
            return pjp.proceed();
        } finally {
            // lock 해제
            lockService.unlock(key);
        }
    }
}
