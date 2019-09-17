package com.manage.annotation;

import java.lang.annotation.*;

/**
 * @Classname NoRepeat
 * @Description TODO
 * @Date 2019/8/31 15:00
 * @Created by hhq
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
// 用于属性的注解
@Target(value= ElementType.FIELD)
public @interface NoRepeat {
    String  desc() default " ";
}
