package com.sparta.todoapp.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j(topic = "로그인 모니터링 AOP")
@Aspect
@Component
public class LoginMonitoringAop {

    //    @Pointcut("execution(* com.aop.controller..*.*(..))")
//    @Pointcut("execution(* com.sparta.todoapp.security..*(..))")
    @Pointcut("execution(* com.sparta.todoapp.security.JwtAuthenticationFilter.attemptAuthentication(..))")
    private void attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {}

//    @Before("execution(* com.sparta.todoapp.security.JwtAuthenticationFilter.attemptAuthentication(request, response))")
    @Before("attemptAuthentication(request, response))")
    public void beforeAttemptAuthentication() {
        log.info("attemp 메서드가 호출되기 전에 AOP 기능 수행");
    }

    @Before("postMapping()")
    public void beforePostMethod(JoinPoint pjp) {
        log.info("post mapping");
    }
}
