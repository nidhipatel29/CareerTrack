package com.CareerTrack.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger =
            Logger.getLogger(getClass().getName());

   @Before("execution(* com.CareerTrack.service.*.*(..))")
public void logBeforeServiceMethod(JoinPoint joinPoint) {
    System.out.println(
        "AOP TEST >>> " + joinPoint.getSignature().toShortString()
    );
}
}