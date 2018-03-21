package me.yummykang.util;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-19 下午2:55
 */
public class ClassUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取当前线程的ClassLoader
     *
     * @return 当前线程的ClassLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 根据className指定的文件载入class信息
     *
     * @param className class名称
     * @param isInitialized 是否需要初始化
     *                      if {@code true} the class will be initialized.
     *                      See Section 12.4 of <em>The Java Language Specification</em>.
     * @return
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls = null;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("class不存在：" + className, e);
        }
        return cls;
    }


    /**
     * 获取指定包下的所有class信息
     *
     * @param packageName 包名
     * @return Class对象Set
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<>();
        try {
            // 查询包下的url列表
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    // 获取url的协议，不同的文件有不同的协议
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String packagePath = url.getPath().replaceAll("%20", "");
                        addClass(classSet, packagePath, packageName);
                    } else if ("jar".equals(protocol)) {
                        // 获取连接jar包的connection
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            // 拿到jar文件的引用
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                // 获取文件列表
                                Enumeration<JarEntry> entries = jarFile.entries();
                                while (entries.hasMoreElements()) {
                                    // 通过classLoader加载类信息
                                    JarEntry entry = entries.nextElement();
                                    String jarEntryName = entry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("packageName不存在：" + packageName, e);
        }

        return classSet;
    }

    /**
     * 添加指定路径下的class对象
     *
     * @param classSet 待存储class的set集合
     * @param packagePath 包路径
     * @param packageName 包名
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        // 根据包路径获取文件信息，过滤出文件夹及class文件，文件夹通过递归继续处理
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                // 截取class名称
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
                    // 组装全限定名称，包名+类名
                    className = packageName + "." + className;
                }
                // 加载类信息
                doAddClass(classSet, className);
            } else {
                // 如果file是一个文件夹，则子路径是文件名
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {

                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }

    }

    /**
     * 载入class信息并添加至set
     *
     * @param classSet 待存储class的set集合
     * @param className 类全路径名称
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        classSet.add(loadClass(className, false));
    }
}
