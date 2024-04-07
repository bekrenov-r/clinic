package com.bekrenov.clinic.aspect;

import com.bekrenov.clinic.util.RequestUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class LoggingAspect {
    private static final String MESSAGE = "Executing request on endpoint {} - [{}]";

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {}

    @Before("restController()")
    public void logEndpointExecution(JoinPoint joinPoint){
        String requestURI = RequestUtils.getRequest().getRequestURI();
        String methodSignature = joinPoint.getSignature().toLongString();

        log.info(MESSAGE, requestURI, methodSignature);
    }
}
