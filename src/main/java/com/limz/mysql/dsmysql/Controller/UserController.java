package com.limz.mysql.dsmysql.Controller;

import com.limz.mysql.dsmysql.Entity.User;
import com.limz.mysql.dsmysql.Service.UserService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "获取用户信息服务")
@RequestMapping("/hello")
@Log4j
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping(path = "/getUser/userID/{userId}",method = RequestMethod.POST)
    @ApiOperation("获取用户接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId",value = "用户ID",required = true, dataType = "String",paramType = "path")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求完成"),
            @ApiResponse(code = 400, message = "请求参数错误")
    })
    public List<User> getUser(@PathVariable String userId){
        User user = new User();
        user.setUserId(userId);
        List<User> users = this.userService.getUser(user);
        return users;
    }

    @RequestMapping(path = "/addUser",method = RequestMethod.POST)
    @ApiOperation("获取用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户ID",required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "userName",value = "用户名",required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "msg",value = "信息",required = true, dataType = "String",paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求完成"),
            @ApiResponse(code = 400, message = "请求参数错误")
    })
    public String addUser(String userId, String userName, String msg){
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setMsg(msg);

        return this.userService.saveAndUpdateUser(user);
    }
}
