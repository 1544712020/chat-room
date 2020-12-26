package edu.hncst.lchat.home.listener;

import edu.hncst.lchat.home.entity.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@WebListener
public class MyServletContextListener implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        HashMap<User, HttpSession> userMap = new HashMap<>();
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("userMap",userMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}