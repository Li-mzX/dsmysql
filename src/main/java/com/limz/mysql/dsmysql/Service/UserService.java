package com.limz.mysql.dsmysql.Service;

import com.limz.mysql.dsmysql.Entity.User;

import java.util.List;

public interface UserService {

    List<User> getUser(User user);

    String saveAndUpdateUser(User user);
}
