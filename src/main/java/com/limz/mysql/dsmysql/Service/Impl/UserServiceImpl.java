package com.limz.mysql.dsmysql.Service.Impl;

import com.limz.mysql.dsmysql.Dao.UserDao;
import com.limz.mysql.dsmysql.Entity.User;
import com.limz.mysql.dsmysql.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public List<User> getUser(User user) {
        List<User> users = this.userDao.query(user,"sr");
        System.out.println(users);
        return users;
    }

    @Override
    public String saveAndUpdateUser(User user) {
        this.userDao.save(user);
        return user.getUserId();
    }


}
