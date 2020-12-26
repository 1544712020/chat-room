package edu.hncst.lchat.home.utils;

import org.springframework.util.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class BaseServlet extends HttpServlet {
    private static HashMap<String, Method> methods = new HashMap<>();  //保存方法提高效率

    /**
     * 生命周期 init(),service(),destroy()
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //设置字符编码
        req.setCharacterEncoding("utf-8");
        //设置响应参数
        resp.setContentType("text/html;charset=utf-8");
        //获取请求的方法名
        String methodName = req.getParameter("method");
        if (ObjectUtils.isEmpty(methodName)) {
            methodName = "execute";
        }
        Class clazz = this.getClass();
        try {
            Method method;
            if (methods.containsKey(methodName)) {
                method = methods.get(methodName);
            } else {
                //通过反射获取方法
                method = clazz.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
                methods.put(methodName, method);
            }
            //执行方法
            method.setAccessible(true);  //访问私有方法
            String result = (String) method.invoke(this, req, resp);
            if (!ObjectUtils.isEmpty(result)) {
                req.getRequestDispatcher(result).forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
