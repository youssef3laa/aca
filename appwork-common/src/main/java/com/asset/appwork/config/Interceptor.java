package com.asset.appwork.config;

import com.asset.appwork.annotations.Action;
import com.asset.appwork.model.Audit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

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
        @Around("execution(* com.asset.appwork.controller.*.*(..)) && args(username,..)")
        public Object inWebLayer(ProceedingJoinPoint joinPoint,Object username) throws Throwable {
            long startTime=0, endtime =0;
            Object object = null; // before
            try {
                startTime = System.currentTimeMillis();
                log.info("Invoked: " + niceName(joinPoint));
                object = joinPoint.proceed();
//                createAudit(joinPoint,object);

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
//        private void createAudit(JoinPoint joinPoint ,Object object){
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            Method method = signature.getMethod();
//            Action actionAnnotation = method.getAnnotation(Action.class);
//            String actionName = actionAnnotation.name();
//            String responseCode = ((ResponseEntity) object).getStatusCode().toString();
//            Audit audit = new Audit();
//            audit.setAction(actionName);
//            audit.setResponseCode(responseCode);
//        }
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
