package me.yummykang.servlet;

import me.yummykang.annotation.Route;
import me.yummykang.constant.RequestMethod;
import me.yummykang.context.ClassContext;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 下午6:51
 */
public class HandlerMapping {
    private static final Map<Request, Handler> REQUEST_MAPPING = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(HandlerMapping.class);


    /**
     * 加载servlet路由信息到内存中
     *
     */
    static {
        Set<Class<?>> classSet = ClassContext.getControllerClass();
        if (CollectionUtils.isNotEmpty(classSet)) {
            for (Class<?> clazz : classSet) {
                Method[] methods = clazz.getDeclaredMethods();
                if (methods != null && methods.length != 0) {
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Route.class)) {
                            Route action = method.getAnnotation(Route.class);
                            String mapping = action.value();
                            RequestMethod requestMethod = action.method();
                            Request request = new Request(requestMethod.getMethod(), mapping);
                            Handler handler = new Handler(clazz, method);
                            REQUEST_MAPPING.put(request, handler);
                            logger.info("载入路由信息：" + mapping);
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(Request request) {
        return REQUEST_MAPPING.get(request);
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return REQUEST_MAPPING.get(request);
    }
}
