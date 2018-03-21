package me.yummykang.ioc;

import me.yummykang.context.BeanContext;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 下午6:28
 */
public class IocHandler {
    /**
     * 检索bean容器中所有标注了@Resource注解的属性的类，并为其实例化赋值
     *
     */
    static {
        Map<Class<?>, Object> beanMap = BeanContext.getBeanMap();
        if (!CollectionUtils.sizeIsEmpty(beanMap)) {
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
                Class<?> cls = entry.getKey();
                Object instance = entry.getValue();
                Field[] fields = cls.getDeclaredFields();
                if (!ArrayUtils.isEmpty(fields)) {
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Resource.class)) {
                            Class<?> fieldType = field.getType();
                            Object fieldInstance = null;
                            try {
                                fieldInstance = BeanContext.getBean(fieldType);
                                if (fieldInstance != null) {
                                    BeanUtils.setProperty(instance, field.getName(), fieldInstance);
                                }
                            } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
