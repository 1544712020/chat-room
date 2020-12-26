package edu.hncst.lchat.home.service.impl;

import edu.hncst.lchat.home.entity.User;
import edu.hncst.lchat.home.service.UserService;
import edu.hncst.lchat.home.dao.UserDao;
import edu.hncst.lchat.home.dao.impl.UserDaoImpl;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public User queryUser(User user) {
       return userDao.queryUser(user);
    }

    @Override
    public User findUserByID(String id) {
        User user = userDao.findUserById(id);
        return user;
    }
}
