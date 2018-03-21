package me.yummykang.config;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 上午11:22
 */
public class YummyRequiredProperties {
    private static final String CONFIG_FILE = "yummy.properties";

    private static final String JDBC_DRIVER_STR = "yummy.framework.jdbc.driver";

    private static final String JDBC_URL_STR = "yummy.framework.jdbc.url";

    private static final String JDBC_USERNAME_STR = "yummy.framework.jdbc.username";

    private static final String JDBC_PWD_STR = "yummy.framework.jdbc.password";

    private static final String APP_BASE_PACKAGE_STR = "yummy.framework.app.base_package";

    private static final String APP_JSP_PATH_STR = "yummy.framework.app.jsp_path";

    private static final String APP_ASSET_PATH_STR = "yummy.framework.app.asset_path";

    public static final String JDBC_DRIVER;

    public static final String JDBC_URL;

    public static final String JDBC_USERNAME;

    public static final String JDBC_PWD;

    public static final String APP_BASE_PACKAGE;

    public static final String APP_JSP_PATH;

    public static final String APP_ASSET_PATH;

    static {
        PropertyResolver propertyResoler = new DefaultPropertyResolver(CONFIG_FILE);
        JDBC_DRIVER = propertyResoler.getProperty(JDBC_DRIVER_STR);
        JDBC_URL = propertyResoler.getProperty(JDBC_URL_STR);
        JDBC_USERNAME = propertyResoler.getProperty(JDBC_USERNAME_STR);
        JDBC_PWD = propertyResoler.getProperty(JDBC_PWD_STR);
        APP_BASE_PACKAGE = propertyResoler.getProperty(APP_BASE_PACKAGE_STR);
        APP_JSP_PATH = propertyResoler.getProperty(APP_JSP_PATH_STR);
        APP_ASSET_PATH = propertyResoler.getProperty(APP_ASSET_PATH_STR);
    }
}
