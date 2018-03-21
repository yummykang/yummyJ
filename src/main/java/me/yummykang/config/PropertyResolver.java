package me.yummykang.config;

/**
 * 获取property文件接口.
 *
 * @author demon
 * @date 18-3-19 上午10:34
 */
public interface PropertyResolver {
    /**
     * 是否包含否个属性，如果key不为{@code null}
     *
     * @param key the properties's key
     * @return true if contained
     */
    boolean containProperty(String key);

    /**
     * 返回key对应的value值，如果找不到对应的value则返回null
     *
     * @param key the properties's key
     * @return key对应的value
     */
    String getProperty(String key);

    /**
     * 返回key对应的value值，如果找不到对应的value则返回defaultValue
     *
     * @param key the properties's key
     * @param defaultValue key的默认值
     * @return key对应的value
     */
    String getProperty(String key, String defaultValue);

    /**
     * 返回key对应的value值，如果找不到对应的value则返回null
     *
     * @param key the properties's key
     * @param targetType value的class类型
     * @return key对应的value
     * @see #getProperty(String, Class, T)
     */
    <T> T getProperty(String key, Class<T> targetType);

    /**
     * 返回key对应的value值，如果找不到对应的value则返回defaultValue
     *
     * @param key the properties's key
     * @param targetType value的class类型
     * @param defaultValue key的默认值
     * @return key对应的value
     */
    <T> T getProperty(String key, Class<T> targetType, T defaultValue);

    /**
     * 获取必须存在的key，如果不存在即抛出IllegalStateException异常
     *
     * @param key the properties's key
     * @return key对应的value
     * @throws IllegalStateException
     * @see #getRequiredProperty(String, Class)
     */
    String getRequiredProperty(String key) throws IllegalStateException;

    /**
     * 获取必须存在的key，如果不存在即抛出IllegalStateException异常
     *
     * @param key the properties's key
     * @param targetType value的class类型
     * @return key对应的value
     * @throws IllegalStateException
     */
    <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException;

    /**
     * 通配符解析，如果给定的text中有${...}通配符，则解析之，如果不能解析，则不对text做额外的操作
     *
     * @param text 待解析的字符串
     * @return 解析成功的字符串
     */
    String resolvePlaceholders(String text);

    /**
     * 通配符解析，如果给定的text中有${...}通配符，则解析之，如果不能解析，则抛出IllegalArgumentException异常
     *
     * @param text 待解析的字符串
     * @return 解析成功的字符串
     * @throws IllegalArgumentException
     */
    String resolveRequiredPlaceholders(String text) throws IllegalArgumentException;
}
