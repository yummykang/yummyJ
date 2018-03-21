package me.yummykang.context;

import me.yummykang.annotation.Controller;
import me.yummykang.config.YummyRequiredProperties;
import me.yummykang.util.ClassUtil;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 下午5:09
 */
public class ClassContext {
    public static final Set<Class<?>> CLASS_SET = ClassUtil.getClassSet(YummyRequiredProperties.APP_BASE_PACKAGE);

    /**
     * 获取service层class
     *
     * @return class列表
     */
    public static Set<Class<?>> getServiceClass() {
        return getSpecifiedClassSet(Resource.class);
    }

    /**
     * 获取controller层class
     *
     * @return class列表
     */
    public static Set<Class<?>> getControllerClass() {
        return getSpecifiedClassSet(Controller.class);
    }

    /**
     * 获取指定类型的class列表
     *
     * @param annotationClass 添加的注解
     * @return class列表
     */
    public static Set<Class<?>> getSpecifiedClassSet(Class annotationClass) {
        Set<Class<?>> result = new HashSet<>();
        for (Class cls : CLASS_SET) {
            if (cls.isAnnotationPresent(annotationClass)) {
                result.add(cls);
            }
        }
        return result;
    }

    /**
     * 获取class列表
     *
     * @return class列表
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }
}
