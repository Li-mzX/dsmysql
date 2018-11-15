package com.limz.mysql.dsmysql.Dao;


import com.limz.mysql.dsmysql.Entity.User;
import com.limz.mysql.dsmysql.Filter.DataBaseShard.TableShard;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 需要分区的dao 需要加上  @TableShard 注解
 */
@TableShard
public interface UserDao{


    @Insert({"insert into user(userId, userName, msg) values(#{userId}, #{userName}, #{msg})"})
    @Options(keyProperty = "userId",keyColumn = "userId")
    void save(User user);


    /**
     * 需要分区的方法参数中，  必须存在 @Param("shardName") 的参数， 或者 存在实体类参数 实现了 ShardEntity 接口  如下面的 User
     * @param user
     * @param shardName
     * @return
     */
    @Select("<script> select * from user where userId=#{user.userId} <if test=\"user.userName != null\"> and userName = #{user.userName}</if> </script>")
    @Results({
            @Result(property = "userId",column = "userId"),
            @Result(property = "userName",column = "userName"),
            @Result(property = "msg",column = "msg"),
            @Result(property = "telephones", javaType = List.class, column = "{userId=userId, shardName=shardName}", many = @Many(select = "com.limz.mysql.dsmysql.Dao.TelephoneDao.findTelephoneByUserId"))})
    List<User> query(@Param("user") User user, @Param("shardName") String shardName);


//    @Select("<script> select * from user where userId=#{user.userId} <if test=\"user.userName != null\"> and userName = #{user.userName}</if> </script>")
//    @Results({
//            @Result(property = "userId",column = "userId"),
//            @Result(property = "userName",column = "userName"),
//            @Result(property = "msg",column = "msg"),
//            @Result(property = "telephones", javaType = List.class, column = "{userId=userId, shardName=shardName}", many = @Many(select = "com.limz.mysql.dsmysql.Dao.TelephoneDao.findTelephoneByUserId"))})
//    List<User> query(@Param("user") User user);


}
