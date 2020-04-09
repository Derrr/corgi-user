package com.corgi.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class MapperAOP {
    @Pointcut("execution(* com.corgi.mapper..*.*(..))")
    public void executeService() {
    }

    /**
     * 前置方法
     *
     * @param pjp
     */
    @Around("executeService()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature s = (MethodSignature) pjp.getSignature();
        Method method = s.getMethod();
        String uri = method.getName();
        long beginTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        log.debug("MapperAop--" + sdf.format(new Date()) + " 开始执行：" + uri);
        Object result;
        String logs = "";
        result = pjp.proceed();
        logs += uri + " sql共耗时：" + (System.currentTimeMillis() - beginTime) + "ms ";
        log.info(logs);
        return result;
    }
}
