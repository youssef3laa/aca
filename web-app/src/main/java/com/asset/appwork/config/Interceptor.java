package com.asset.appwork.config;

import com.asset.appwork.annotations.Action;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Audit;
import com.asset.appwork.model.User;
import com.asset.appwork.service.AuditService;
import com.asset.appwork.service.OrgChartService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
        @Autowired
        TokenService tokenService;
        @Autowired
        OrgChartService orgChartService;
        @Autowired
        AuditService auditService;

        @Around("execution(* com.asset.appwork.controller.*.*(..))")
        public Object inWebLayer(ProceedingJoinPoint joinPoint) throws Throwable {
            long startTime = 0, endtime = 0;
            Object object = null; // before
            try {

                startTime = System.currentTimeMillis();
                log.info("Invoked: " + niceName(joinPoint));
                object = joinPoint.proceed();
                createAudit(joinPoint, object);

                endtime = System.currentTimeMillis();
            } finally {
                log.info("Finished execution at: " + (endtime - startTime) + "ms result: " + "\n\tresults: " + object);
            }

            return object;

        }

        private String niceName(JoinPoint joinPoint) {
            return joinPoint.getTarget().getClass()
                    + "#" + joinPoint.getSignature().getName()
                    + "\n\targs:" + Arrays.toString(joinPoint.getArgs());
        }

        private void createAudit(JoinPoint joinPoint, Object object) {
            Audit audit = new Audit();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            Optional<Action> actionAnnotation = Optional.ofNullable(method.getAnnotation(Action.class));
            AtomicReference<String> actionName = new AtomicReference<>("");
           AtomicInteger actionArgumentNumber = new AtomicInteger(-1);
            actionAnnotation.ifPresentOrElse(action -> {
                actionName.set(action.name());
                actionArgumentNumber.set(action.argumentNumber());
            }, () -> actionName.set(method.getName()));

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Optional<String> token = Optional.ofNullable(request.getHeader("X-Auth-Token"));
            try {
                if (!token.isPresent()) return;
                if(actionArgumentNumber.get() != -1) actionName.set(actionName.get()+"-"+joinPoint.getArgs()[actionArgumentNumber.get()]);
                Account account = tokenService.get(token.get());
                User user = orgChartService.getLoggedInUser(account);
                String userCN = user.getCN();
                audit.setUserCN(userCN);
                String responseCode = ((ResponseEntity) object).getStatusCode().toString();
                audit.setTakenAction(actionName.get());
                audit.setResponseCode(responseCode);
                auditService.createAudit(token.get(), audit);
            } catch (AppworkException e) {
                e.printStackTrace();
            }

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
            } finally {

            }
            return object;

        }
    }
}
