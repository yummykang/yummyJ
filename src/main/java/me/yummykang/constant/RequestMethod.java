package me.yummykang.constant;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 下午6:54
 */
public enum RequestMethod {
    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete");

    private String method;

    RequestMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
