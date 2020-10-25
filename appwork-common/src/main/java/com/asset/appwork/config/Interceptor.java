package com.asset.appwork.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by karim on 10/22/20.
 * this class used for intercept requests
 */
@Slf4j
@Component
public class Interceptor {
    /**
     * Created by karim on 10/19/20.
     * Aspect for intercept any controller call
     */
    @Component
    @Aspect
    public class Logging {
        @Around("execution(* com.asset.appwork.controller.*.*(..))")
        public Object inWebLayer(ProceedingJoinPoint joinPoint) throws Throwable {
            long startTime=0, endtime =0;
            Object object = null; // before
            try {
                startTime = System.currentTimeMillis();
                log.info("Invoked: " + niceName(joinPoint));
                object = joinPoint.proceed();
                endtime = System.currentTimeMillis();
            }finally {
                log.info("Finished execution at: "+(endtime-startTime)+"ms result: " +  "\n\tresults: " + object);
            }

            return object;

        }

        private String niceName(JoinPoint joinPoint) {
            return joinPoint.getTarget().getClass()
                    + "#" + joinPoint.getSignature().getName()
                    + "\n\targs:" + Arrays.toString(joinPoint.getArgs());
        }
    }


    /**
     * Aspect for intercept any repository call
     */
    @Aspect
    @Component
    public class RepositoryInterceptor {


        @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
        public Object inWebLayer(ProceedingJoinPoint joinPoint) throws Throwable {
            Object object = null; // before
            try {
                object = joinPoint.proceed();
            }finally {

            }
            return object;

        }
    }
}
