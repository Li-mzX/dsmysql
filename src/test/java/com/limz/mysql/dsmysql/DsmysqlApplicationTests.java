package com.limz.mysql.dsmysql;

import com.limz.mysql.dsmysql.Dao.UserDao;
import com.limz.mysql.dsmysql.Entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DsmysqlApplicationTests {

    @Autowired
    private UserDao dao;

    @Test
    public void contextLoads() {
        User user = new User();
        user.setUserId("sr000001");
    }

}
