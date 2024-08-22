package com.example.owoonwan.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GroupLock {
    long tryLockTime() default 3000;
}
