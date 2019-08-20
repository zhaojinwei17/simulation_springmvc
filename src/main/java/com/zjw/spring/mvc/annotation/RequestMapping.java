package com.zjw.spring.mvc.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})  //声明该注解的运行目标:接口、类、枚举、注解、方法
@Retention(value = RetentionPolicy.RUNTIME)     //该注解的生命周期: 运行时
public @interface RequestMapping {
    String value() default "";
    String type() default "GET";
}
