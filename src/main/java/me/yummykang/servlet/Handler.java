package me.yummykang.servlet;

import java.lang.reflect.Method;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 下午6:48
 */
public class Handler {
    private Class<?> ctlClass;

    private Method actionMethod;

    public Handler(Class<?> ctlClass, Method actionMethod) {
        this.ctlClass = ctlClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getCtlClass() {
        return ctlClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
