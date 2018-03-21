package me.yummykang.servlet;

import me.yummykang.App;
import me.yummykang.config.YummyRequiredProperties;
import me.yummykang.context.BeanContext;
import me.yummykang.util.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.lf5.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * write something to describe this file.
 *
 * @author demon
 * @date 18-3-20 上午10:00
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.info("开始初始化DispatcherServlet");
        // 初始化基础框架
        App.init();

        // 获取servlet应用上下文
        ServletContext servletContext = config.getServletContext();

        // 注册jsp的处理
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(YummyRequiredProperties.APP_JSP_PATH + "*");

        // 注册静态资源
        ServletRegistration defaultServelt = servletContext.getServletRegistration("default");
        defaultServelt.addMapping(YummyRequiredProperties.APP_ASSET_PATH + "*");
        logger.info("初始化DispatcherServlet成功");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod().toLowerCase();

        String requestPath = req.getPathInfo();

        Handler handler = HandlerMapping.getHandler(requestMethod, requestPath);

        if (handler != null) {
            Class<?> cls = handler.getCtlClass();
            Map<String, Object> paramsMap = new HashMap<>();
            try {
                Object controller = BeanContext.getBean(cls);
                // 解析post或者put等body中的请求参数
                Enumeration<String> paramNames = req.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    String paramName = paramNames.nextElement();
                    String paramValue = req.getParameter(paramName);
                    paramsMap.put(paramName, paramValue);
                }
                // 解析url中的参数
                String body = URLDecoder.decode(new String(StreamUtils.getBytes(req.getInputStream())), "UTF-8");
                if (StringUtils.isNotEmpty(body)) {
                    String[] params = StringUtils.split(body, "&");
                    if (!ArrayUtils.isEmpty(params)) {
                        for (String param : params) {
                            String[] paramCouple = StringUtils.split(param, "=");
                            if (!ArrayUtils.isEmpty(paramCouple) && paramCouple.length == 2) {
                                String paramName = paramCouple[0];
                                String paramValue = paramCouple[1];
                                paramsMap.put(paramName, paramValue);
                            }
                        }
                    }
                }

                // 获取handler的route方法
                Method routeMethod = handler.getActionMethod();

                // 获取方法的参数
                Class<?>[] classes = routeMethod.getParameterTypes();
                // 此参数用于统计方法的参数由几个类型及什么的类型组成，如果是普通类型加1,如果是request加2,如果是response加4
                int parameterTypeCount = 0;
                // 获取自定义的pojo请求类，并将参数填充进pojo
                Object pojoRequest = null;
                for (Class clazz : classes) {
                    if (!clazz.isInstance(req) && !clazz.isInstance(resp)) {
                        pojoRequest = clazz.newInstance();
                        parameterTypeCount++;
                    } else if (clazz.isInstance(req)) {
                        parameterTypeCount += 2;
                    } else if (clazz.isInstance(resp)) {
                        parameterTypeCount += 4;
                    }
                }
                BeanUtils.populate(pojoRequest, paramsMap);

                // 调用此方法
                Object result = null;
                switch (parameterTypeCount) {
                    // 表示没有任何参数
                    case 0:
                        result = routeMethod.invoke(controller);
                        break;
                    // 表示仅仅包含了pojoRequest
                    case 1:
                        result = routeMethod.invoke(controller, pojoRequest);
                        break;
                    // 表示仅仅包含了req
                    case 2:
                        result = routeMethod.invoke(controller, req);
                        break;
                    // 表示包含了pojoRequest+req
                    case 3:
                        result = routeMethod.invoke(controller, pojoRequest, req);
                        break;
                    // 表示仅仅包含了response
                    case 4:
                        result = routeMethod.invoke(controller, resp);
                        break;
                    // 表示包含了req+resp
                    case 6:
                        result = routeMethod.invoke(controller, req, resp);
                        break;
                    // 表示包含了pojoRequest+req+resp
                    case 7:
                        result = routeMethod.invoke(controller, pojoRequest, req, resp);
                        break;
                    default:
                        break;

                }

                // 针对结果进行操作，是视图还是返回data数据，暂时只做json返回
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                String json = JsonUtil.toJson(result);
                out.write(json);
                out.flush();
                out.close();
            } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            logger.error(requestPath + "找不到对应的handler");
        }

    }
}
