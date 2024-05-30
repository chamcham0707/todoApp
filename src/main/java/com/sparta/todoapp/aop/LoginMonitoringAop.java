package com.sparta.todoapp.aop;

import com.sparta.todoapp.dto.LoginRequestDto;
import com.sparta.todoapp.entity.LoginPerformEnum;
import com.sparta.todoapp.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j(topic = "로그인 모니터링 AOP")
@Aspect
@Component
@RequiredArgsConstructor
public class LoginMonitoringAop {

    private final LoginLogService loginLogService;

    @Before("execution(* com.sparta.todoapp.controller.UserController.login(..))")
    public void loginBefore(JoinPoint joinPoint) {
        log.info("로그인 전");

        Object[] args = joinPoint.getArgs();
        LoginRequestDto requestDto = (LoginRequestDto) args[0];

        loginLogService.saveLoginLog(requestDto, LoginPerformEnum.BEFORE);
    }

    @AfterReturning("execution(* com.sparta.todoapp.controller.UserController.login(..))")
    public void loginSuccess(JoinPoint joinPoint) {
        log.info("로그인 성공");

        Object[] args = joinPoint.getArgs();
        LoginRequestDto requestDto = (LoginRequestDto) args[0];

        loginLogService.saveLoginLog(requestDto, LoginPerformEnum.SUCCESS);
    }

    @AfterThrowing("execution(* com.sparta.todoapp.controller.UserController.login(..))")
    public void loginFailure(JoinPoint joinPoint) {
        log.info("로그인 실패");

        Object[] args = joinPoint.getArgs();
        LoginRequestDto requestDto = (LoginRequestDto) args[0];

        loginLogService.saveLoginLog(requestDto, LoginPerformEnum.FAILURE);
    }
}
