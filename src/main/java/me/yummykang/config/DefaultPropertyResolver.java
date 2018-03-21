package me.yummykang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;


/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 上午11:07
 */
public class DefaultPropertyResolver extends AbstractPropertyResolver {
    private static final Logger logger = LoggerFactory.getLogger(DefaultPropertyResolver.class);

    private Properties properties;

    public DefaultPropertyResolver(String fileName) {
        load(fileName);
    }

    public <T> T getProperty(String key, Class<T> targetType) {
        T result = (T) properties.getProperty(key);
        return result;
    }

    public String resolvePlaceholders(String text) {
        return null;
    }

    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return null;
    }

    protected void load(String fileName) {
        this.properties = new Properties();
        try {
            this.properties.load(DefaultPropertyResolver.class.getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("配置文件:" + fileName + "不存在", e);
        }
    }
}
