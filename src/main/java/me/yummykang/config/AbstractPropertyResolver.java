package me.yummykang.config;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 上午10:50
 */
public abstract class AbstractPropertyResolver implements PropertyResolver {

    public boolean containProperty(String key) {
        return (getProperty(key) != null);
    }

    public String getProperty(String key) {
        return getProperty(key, String.class);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value == null ? defaultValue : value;
    }

    public abstract <T> T getProperty(String key, Class<T> targetType);

    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        T value = getProperty(key, targetType);
        return value == null ? defaultValue : value;
    }

    public String getRequiredProperty(String key) throws IllegalStateException {
        String value = getProperty(key);
        if (value == null) {
            throw new IllegalStateException("key:" + key + "未找到");
        }
        return value;
    }

    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        T value = getProperty(key, targetType);
        if (value == null) {
            throw new IllegalStateException("key:" + key + "未找到");
        }
        return value;
    }

    public abstract String resolvePlaceholders(String text);

    public abstract String resolveRequiredPlaceholders(String text) throws IllegalArgumentException;
}
