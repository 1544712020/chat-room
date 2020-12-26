package edu.hncst.lchat.home.dao.impl;

import edu.hncst.lchat.home.entity.User;
import edu.hncst.lchat.home.utils.basedao.impl.BaseDaoImpl;
import edu.hncst.lchat.home.dao.UserDao;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
    @Override
    public User queryUser(User user) {
        String sql = "select * from user where username = ? and password = ?";
        try {
            User result = queryRunner.query(sql,new BeanHandler<>(User.class),user.getUsername(),user.getPassword());
            return result;
        }catch (Exception e){
            return null;
        }
    }


    @Override
    public User findUserById(String id) {
        String sql = "SELECT * from user where id = ?";
        try {
            User result1 = queryRunner.query(sql,new BeanHandler<>(User.class), id);
            return result1;
        }catch (Exception e){
            return null;
        }
    }
}
