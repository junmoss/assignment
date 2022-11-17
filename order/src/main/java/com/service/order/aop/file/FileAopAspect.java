package com.service.order.aop.file;

import com.service.order.error.exception.file.FileServiceException;
import com.service.order.error.exception.query.OrderServiceException;
import com.service.order.service.file.FileService;
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
public class FileAopAspect {
    private final FileService fileService;

    @Around("@annotation(com.service.order.aop.file.FileTransaction)")
    public Object fileRollBackMethod(
            ProceedingJoinPoint pjp
    ) throws Throwable {
        try {
            fileService.writeBackUpFile();
        } catch (FileServiceException e) {
            log.error("[FileAopAspect:fileRollBackMethod] enter {}", e.getMessage());
        }

        try {
            return pjp.proceed();
        } catch (Exception e) {
            if (e instanceof FileServiceException || e instanceof OrderServiceException) {
                try {
                    fileService.restoreBackUpFile();
                } catch (Exception exception) {
                    log.error("[FileAopAspect:fileRollBackMethod] exception {}", e.getMessage());
                }
            }
            throw e;
        }
    }
}
