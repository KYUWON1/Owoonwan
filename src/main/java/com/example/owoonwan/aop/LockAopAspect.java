package com.example.owoonwan.aop;

import com.example.owoonwan.exception.RedissonException;
import com.example.owoonwan.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final RedissonClient redissonClient;

    @Around("@annotation(lock) && args(groupId,..)")
    public Object aroundMethod(ProceedingJoinPoint pjp, GroupJoinLock lock, Long groupId) throws Throwable {
        String lockKey = "workout_group_join_lock_" + groupId;
        RLock rLock = redissonClient.getLock(lockKey);
        boolean isLocked = false;
        try {
            isLocked = rLock.tryLock(2, 10, TimeUnit.SECONDS);
            if (!isLocked) {
                log.info("can't get Lock {}",lock);
                throw new RedissonException(ErrorCode.FAIL_LOCK_ACQUIRE);
            }
            log.info("get Lock {}",lock);
            // 동시성 테스트용
            //Thread.sleep(10000);  // 10초 동안 대기
            return pjp.proceed();
        } finally {
            if (isLocked && rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                log.info("Lock released for groupId: {}", groupId);
            }
        }
    }
}
