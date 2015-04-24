package com.kjt.service.#{artifactId}.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.kjt.service.common.AcessLoger;

@Aspect
public class LogAspect {
    /**
     * execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?
     * name-pattern(param-pattern)throws-pattern?) returning type pattern,name pattern, and parameters
     * pattern是必须的.<br>
     * ret-type-pattern:可以为*表示任何返回值,全路径的类名等.<br>
     * name-pattern:指定方法名,*代表所以,set*,代表以set开头的所有方法.<br>
     * parameters pattern:指定方法参数(声明的类型),(..)代表所有参数,(*)代表一个参数,(*,String)代表第一个参数为任何值 ,第二个为String类型.<br>
     */
    @Around("execution(@com.kjt.service.common.aop.LoggerPoint * * (..))")
    public Object processAround(ProceedingJoinPoint pjp) throws Throwable {
      return AcessLoger.process(pjp);
    }
}
