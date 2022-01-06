package com.cybertek.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // this annotation will work at method level
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultExceptionMessage {

    String defaultMessage() default "";
}