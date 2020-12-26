package edu.hncst.lchat.home.service;

import edu.hncst.lchat.home.entity.User;

public interface UserService {
    User queryUser(User user);

    User findUserByID(String id);
}
