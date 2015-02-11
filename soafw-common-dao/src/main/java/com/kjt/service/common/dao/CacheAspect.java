/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kjt.service.common.dao;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.kjt.service.common.cache.CacheOpParamsContext;
import com.kjt.service.common.cache.annotation.CacheOpParams;

@Aspect
public class CacheAspect {
  /**
   * execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?
   * name-pattern(param-pattern)throws-pattern?) returning type pattern,name pattern, and parameters
   * pattern是必须的.<br>
   * ret-type-pattern:可以为*表示任何返回值,全路径的类名等.<br>
   * name-pattern:指定方法名,*代表所以,set*,代表以set开头的所有方法.<br>
   * parameters pattern:指定方法参数(声明的类型),(..)代表所有参数,(*)代表一个参数,(*,String)代表第一个参数为任何值 ,第二个为String类型.<br>
   */
  @Around("execution(@com.anjuke.service.common.cache.annotation.CacheOpParams * * (..))")
  public Object processAround(ProceedingJoinPoint pjp) throws Throwable {
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    CacheOpParams cacheOpParams = method.getAnnotation(CacheOpParams.class);
    if (cacheOpParams != null) {
      CacheOpParamsContext.setOpParams(cacheOpParams);
    }
    return pjp.proceed();
  }
}
