package edu.hncst.lchat.home.entity;

import edu.hncst.lchat.home.utils.basedao.ColumnName;
import edu.hncst.lchat.home.utils.basedao.TableName;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@TableName("user")
public class User implements Serializable, HttpSessionBindingListener {
    @ColumnName("id")
    private int id;
    @ColumnName("username")
    private String username;
    @ColumnName("password")
    private String password;
    @ColumnName("msg")
    private String msg;
    @ColumnName("from_time")
    private Date fromTime;
    @ColumnName("type")
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
        HttpSession session = httpSessionBindingEvent.getSession();
        Map<User,HttpSession> userMap = (Map<User, HttpSession>) session.getServletContext().getAttribute("userMap");
        userMap.put(this,session);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {
        HttpSession session = httpSessionBindingEvent.getSession();
        Map<User,HttpSession> userMap = (Map<User, HttpSession>) session.getServletContext().getAttribute("userMap");
        userMap.remove(this);
    }
}
