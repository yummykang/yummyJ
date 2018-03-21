package me.yummykang;

import me.yummykang.context.BeanContext;
import me.yummykang.context.ClassContext;
import me.yummykang.ioc.IocHandler;
import me.yummykang.servlet.HandlerMapping;
import me.yummykang.util.ClassUtil;

/**
 * Hello world!
 */
public class App {
    public static void init() {
        Class<?>[] clses = {
                ClassContext.class,
                BeanContext.class,
                IocHandler.class,
                HandlerMapping.class
        };

        for (Class<?> cls : clses) {
            ClassUtil.loadClass(cls.getName(), true);
        }

    }
}
