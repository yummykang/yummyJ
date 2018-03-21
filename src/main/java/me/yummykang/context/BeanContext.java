package me.yummykang.context;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 下午6:04
 */
public class BeanContext {
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        for (Class cls : ClassContext.CLASS_SET) {
            try {
                if (!cls.isInterface() && !cls.isArray() && !cls.isPrimitive() && !cls.isEnum() && !Modifier.isAbstract(cls.getModifiers()) && hasParameterlessConstructor(cls)) {
                    BEAN_MAP.put(cls, cls.newInstance());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取bean容器
     *
     * @return bean容器
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 返回指定的bean实例
     *
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> cls) throws ClassNotFoundException {
        T t = (T) BEAN_MAP.get(cls);
        if (t == null) {
            throw new ClassNotFoundException("没有找到类在bean容器中：" + cls.getName());
        }
        return t;
    }

    private static boolean hasParameterlessConstructor(Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .anyMatch((c) -> c.getParameterCount() == 0);
    }
}
