package edu.hncst.lchat.home.controller;

import com.sun.javafx.binding.StringFormatter;
import edu.hncst.lchat.home.entity.User;
import edu.hncst.lchat.home.service.UserService;
import edu.hncst.lchat.home.dao.UserDao;
import edu.hncst.lchat.home.dao.impl.UserDaoImpl;
import edu.hncst.lchat.home.service.impl.UserServiceImpl;
import edu.hncst.lchat.home.utils.BaseServlet;
import javafx.scene.input.DataFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@WebServlet("/user")
public class LoginServlet extends BaseServlet {

    UserDao userDao = new UserDaoImpl();

    /**
     * 检测踢下线自动刷新
     * @param request
     * @param response
     * @return
     */
    public String check(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 从session中获取用户的信息
        User existUser = (User) request.getSession().getAttribute("existUser");
        // 从session中判断用户是否过期
        if (existUser == null) {
            // 用户已过期
            response.getWriter().println("1");
        } else {
            // 用户登录信息没有过期
            response.getWriter().println("2");
        }
        return null;
    }

    /**
     * 退出操作
     * @param request
     * @param response
     * @return
     */
    public String exit(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        // 将session销毁
        session.invalidate();
        try {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 显示聊天内容
     * @param request
     * @param response
     * @return
     */
    public String getMessage(HttpServletRequest request, HttpServletResponse response) {
        // 从application范围内获取聊天记录
        String message = (String)getServletContext().getAttribute("message");
        // 判断聊天内容是否存在
        if (message != null) {
            try {
                // 通过response输出信息
                response.getWriter().println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 发送消息内容
     * @param request
     * @param response
     * @return
     */
    public String sendMessage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("方法已经执行");
        // 接收数据

        // 1.发送者
        String from = request.getParameter("from");

        // 2.接收者
        String to = request.getParameter("to");

        // 3.发送相关信息
        // 表情
        String face = request.getParameter("face");

        // 字体颜色
        String color = request.getParameter("color");

        //内容
        String content = request.getParameter("content");

        // 获取当前时间
        Date date = new Date();
        // 创建时间格式化类
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 将当前时间的格式格式化
        String sendTime = simpleDateFormat.format(date);
        // xx对xx说+内容+发生时间
        // 获取application范围中的userMap对象
        ServletContext application = getServletContext();
        // 从application中取出消息
        String message = (String)application.getAttribute("message");
        message += "<font color='blue'><strong>" + from + "</strong></font>" + "<font color='CC0000'>" + face +
                "</font>" + "对" + "<font color='green'>[" + to +"]</font>" + "说：" + "<font color='"+ color +"'>" + content + "</font>("
                + sendTime + ")<br>";
        // 将消息存放在application中
        application.setAttribute("message", message);
        return getMessage(request, response);
    }

    /**
     * 踢下线功能
     * @param request
     * @param response
     * @return
     */
    public String kick(HttpServletRequest request, HttpServletResponse response) {
        // 接收参数
        String id = request.getParameter("id");
        Integer.parseInt(id);
        // 获取application范围中的userMap对象
        Map<User, HttpSession> userMap = (Map<User, HttpSession>) getServletContext().getAttribute("userMap");
        User user = userDao.findUserById(id);
        HttpSession session = userMap.get(user);
        session.removeAttribute("existUser");
        try {
            response.sendRedirect(request.getContextPath() + "/main.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        Map<String, String[]> map = req.getParameterMap();
        User user = new User();
        BeanUtils.populate(user, map);
        UserService userService = new UserServiceImpl();
        User result = userService.queryUser(user);
        if (ObjectUtils.isEmpty(result)) {
            req.setAttribute("msg", "账号或密码错误！");
            return "index.jsp";
        }
        //添加result到session
        session.setAttribute("existUser", result);
        String sourceMessage = "";
        ServletContext application = getServletContext();
        //message不为空
        if (!ObjectUtils.isEmpty(application.getAttribute("message"))) {
            sourceMessage = (String) application.getAttribute("message");
        }
        sourceMessage += "系统公告：<font color='gray'>" +
                user.getUsername()+ "走进了聊天室！</font><br>";
        //把消息存放在application
        application.setAttribute("message",sourceMessage);
        resp.sendRedirect(req.getContextPath() + "/main.jsp");
        return null;
    }
}
