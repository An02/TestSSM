package com.manage.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @Classname UserAspect
 * @Description TODO
 * @Date 2019/7/30 11:03
 * @Created by hhq
 */
@Aspect
public class UserAspect {
    //匹配所有ServiceImpl包下面的所有类的所有方法
    //@Pointcut("@annotation(* com.manage.XXXX)")匹配包含XXX注解的方法
    @Pointcut("execution(* com.manage.service.serviceImpl.*.*(..))")
    public void addLog(){}


    //@Pointcut("execution(public * *(..))")
    //public void skipMethod(){}
}
