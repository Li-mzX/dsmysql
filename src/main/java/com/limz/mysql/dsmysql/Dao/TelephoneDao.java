package com.limz.mysql.dsmysql.Dao;


import com.limz.mysql.dsmysql.Entity.Telephone;
import com.limz.mysql.dsmysql.Filter.DataBaseShard.TableShard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@TableShard
public interface TelephoneDao{

    @Insert("insert into telephone (userId, telephone) values(#{userId},#{telephone})")
    void save(Telephone t);

    @Select("select * from telephone where userId = #{userId}")
    List<Telephone> findTelephoneByUserId(String shardName, String userId);

    @Select("select * from telephone where id = #{id}")
    Telephone get(Telephone t);
}
