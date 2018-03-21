package me.yummykang.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-20 上午11:23
 */
public class JsonUtil {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static String toJson(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }
}
