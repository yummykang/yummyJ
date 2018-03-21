package me.yummykang.annotation;

import me.yummykang.constant.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 下午4:54
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {
    String value();

    RequestMethod method();
}
