package edu.hncst.lchat.home.dao;

import edu.hncst.lchat.home.entity.User;
import edu.hncst.lchat.home.utils.basedao.BaseDao;

public interface UserDao extends BaseDao {
        User queryUser(User user);
        User findUserById(String id);
}
